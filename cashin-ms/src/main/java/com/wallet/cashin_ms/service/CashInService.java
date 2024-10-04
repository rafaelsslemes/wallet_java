package com.wallet.cashin_ms.service;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wallet.cashin_ms.domain.CashIn;
import com.wallet.cashin_ms.domain.InBox;
import com.wallet.cashin_ms.domain.errors.IdempotencyViolation;
import com.wallet.cashin_ms.domain.errors.UniquenessViolation;
import com.wallet.cashin_ms.repository.CashInRepository;

import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class CashInService implements CashInServiceInterface {

    @Autowired
    private InBoxServiceInterface inBoxService;

    @Autowired
    private BalanceServiceInterface balanceService;

    @Autowired
    private CashInRepository repository;

    @Override
    public void processPendings() {

        List<InBox> pendings = inBoxService.listPending();

        for (InBox inBox : pendings) {

            CashIn cashIn = inBoxService.parsePayload(inBox.getPayload(), CashIn.class);
            
            try {
                this.process(cashIn);
            } catch (Exception e) {
               continue;
            }
        }
    }

    @Override
    @Transactional
    public UUID process(CashIn cashIn) {

        this.validateIdempotency(cashIn);
        this.validateDuplicity(cashIn);

        // Salva a operação
        repository.save(cashIn);

        // Inclui no Outbox
        
        
        // Integra com o serviço que controla o saldo
        balanceService.sendBalanceUpdate(cashIn);

        log.info("CashIn Processed successfully: {}", cashIn.getEventId());

        return cashIn.getEventId();
    }

    // Verifica se uma transação está duplicada dentro do período estabelecido
    private void validateDuplicity(CashIn cashIn) {

        final int DUPLICITY_PERIOD = 5;

        var existent = repository.findBySourceIdAndPayerIdAndReceiverIdAndValue(cashIn.getSourceId(),
                cashIn.getPayerId(), cashIn.getReceiverId(), cashIn.getValue());

        if (existent.isPresent()) {
            Duration duration = Duration.between(cashIn.getDate().toInstant(), existent.get().getDate().toInstant());
            long diff = Math.abs(duration.toMinutes());

            if (diff < DUPLICITY_PERIOD) {
                log.error("CashIn {} violates Idempotency rules. Discarded.", cashIn.getEventId());
                throw new RuntimeException();
            }
        }
    }

    private void validateIdempotency(CashIn cashIn) {

        var existent = repository.findById(cashIn.getEventId());
        if (existent.isPresent()) {

            log.error("CashIn {} violates Idempotency rules. Discarded.", cashIn.getEventId());
            throw new RuntimeException();
        }
    }

}
