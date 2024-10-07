package com.wallet.statement_ms.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

import com.wallet.statement_ms.domain.InBox;
import com.wallet.statement_ms.domain.Transaction;
import com.wallet.statement_ms.domain.errors.UniquenessViolation;
import com.wallet.statement_ms.repository.repository.TransactionRepository;

import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class TransactionService implements TransactionServiceInterface {

    @Autowired
    private InBoxServiceInterface inBoxService;

    @Autowired
    private TransactionRepository repository;

    @Override
    public void processPendings() {

        List<InBox> pendings = inBoxService.listPending();

        for (InBox inBox : pendings) {

            Transaction transaction = inBoxService.parsePayload(inBox.getPayload(), Transaction.class);

            try {
                log.info("PROCESSING TRANSACTION: {}", transaction.getEventId());
                this.process(transaction);
                log.info("PROCESSED TRANSACTION: {}", transaction.getEventId());

            } catch (UniquenessViolation e) {
                inBoxService.complete(inBox.getId());

                // TODO: Emitir retorno negativo em um tópico para as integrações Externas
            } catch (Exception e) {
                log.error(e.getMessage());
                continue;
            }
        }
    }

    
    @Override
    @Transactional(rollbackOn = UniquenessViolation.class)
    public UUID process(Transaction transaction) throws UniquenessViolation {

        this.validateIdempotency(transaction);

        // Salva a operação
        repository.save(transaction);

        log.info("TRANSACTION PROCESSED SUCCESSFULLY: {}", transaction.getEventId());

        return transaction.getEventId();
    }


    private void validateIdempotency(Transaction transaction) throws UniquenessViolation {

        var existent = repository.findById(transaction.getEventId());
        if (existent.isPresent()) {

            log.error("TRANSACTION {} VIOLATES IDEMPOTENCY RULES. DISCARDED.", transaction.getEventId());
            throw new UniquenessViolation();
        }
    }


    @Override
    public List<Transaction> listPaginated(UUID accountId, int page) throws NotFoundException {
        Optional<List<Transaction>> optional = repository.findAllByReceiverIdAndMore(accountId, 20 * page, 20);
        if (!optional.isPresent()) {
            throw new NotFoundException();
        }

        List<Transaction> transactions = optional.get();
        return transactions;
    }

}
