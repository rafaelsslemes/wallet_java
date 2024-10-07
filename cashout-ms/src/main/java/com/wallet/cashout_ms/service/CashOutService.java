package com.wallet.cashout_ms.service;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wallet.cashout_ms.controller.TransactionMessageProducer;
import com.wallet.cashout_ms.domain.CashOut;
import com.wallet.cashout_ms.domain.InBox;
import com.wallet.cashout_ms.domain.OutBox;
import com.wallet.cashout_ms.domain.errors.UniquenessViolation;
import com.wallet.cashout_ms.dto.CashOutDto;
import com.wallet.cashout_ms.dto.UpdateBalanceDto;
import com.wallet.cashout_ms.dto.UpdateBalanceType;
import com.wallet.cashout_ms.repository.CashoutRepository;

import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class CashOutService implements CashOutServiceInterface {

    @Autowired
    private InBoxServiceInterface inBoxService;

    @Autowired
    private OutBoxServiceInterface outBoxService;

    @Autowired
    private BalanceServiceInterface balanceService;

    @Autowired
    private CashoutRepository repository;

    @Autowired
    private TransactionMessageProducer producer;

    @Override
    public void processPendings() {

        List<InBox> pendings = inBoxService.listPending();

        for (InBox inBox : pendings) {

            CashOut cashOut = inBoxService.parsePayload(inBox.getPayload(), CashOut.class);

            try {
                log.info("PROCESSING CASHOUT: {}", cashOut.getEventId());
                this.process(cashOut);
                log.info("PROCESSED CASHOUT: {}", cashOut.getEventId());

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
    public void sendProcessed() {

        List<OutBox> pendings = outBoxService.listPending();

        for (OutBox outBox : pendings) {

            CashOutDto dto = outBoxService.parsePayload(outBox.getPayload(), CashOutDto.class);
            producer.sendCashOut(dto, outBox.getId());
        }
    }

    @Override
    @Transactional(rollbackOn = UniquenessViolation.class)
    public UUID process(CashOut cashOut) throws UniquenessViolation {

        this.validateIdempotency(cashOut);
        this.validateDuplicity(cashOut);

        // Salva a operação
        repository.save(cashOut);

        // Armazena a mensagem na OutBox para enviar posteriormente
        outBoxService.save(cashOut);

        sendUpdateBalance(cashOut);

        log.info("CashOut Processed successfully: {}", cashOut.getEventId());

        return cashOut.getEventId();
    }

    private void sendUpdateBalance(CashOut cashOut) {
        // Integra com o serviço que controla o saldo
        // Ao enviar para Balance trabalhar com concorrrência
        UpdateBalanceDto dto = new UpdateBalanceDto(cashOut.getReceiverId(), UpdateBalanceType.DEBIT,
        cashOut.getValue());

        try {
            balanceService.sendBalanceUpdate(dto, dto.getReceiverId());
        } catch (Exception e) {

            log.error("ERROR ACCESSING BALANCE!");
            throw new RuntimeException();
        }
    }

    // Verifica se uma transação está duplicada dentro do período estabelecido
    private void validateDuplicity(CashOut cashOut) throws UniquenessViolation {

        final int DUPLICITY_PERIOD = 5;

        var existent = repository.findBySourceIdAndPayerIdAndReceiverIdAndValue(cashOut.getSourceId(),
        cashOut.getPayerId(), cashOut.getReceiverId(), cashOut.getValue());

        if (existent.isPresent()) {
            Duration duration = Duration.between(cashOut.getDate().toInstant(), existent.get().getDate().toInstant());
            long diff = Math.abs(duration.toMinutes());

            if (diff < DUPLICITY_PERIOD) {
                log.error("CashOut {} violates Idempotency rules. Discarded.", cashOut.getEventId());
                throw new UniquenessViolation();
            }
        }
    }

    private void validateIdempotency(CashOut cashOut) throws UniquenessViolation {

        Optional<CashOut> existent = repository.findById(cashOut.getEventId());
        if (existent.isPresent()) {

            log.error("CashOut {} violates Idempotency rules. Discarded.", cashOut.getEventId());
            throw new UniquenessViolation();
        }
    }

}
