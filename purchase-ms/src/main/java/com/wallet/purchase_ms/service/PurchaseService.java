package com.wallet.purchase_ms.service;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wallet.purchase_ms.controller.TransactionMessageProducer;
import com.wallet.purchase_ms.domain.Purchase;
import com.wallet.purchase_ms.domain.InBox;
import com.wallet.purchase_ms.domain.OutBox;
import com.wallet.purchase_ms.domain.errors.UniquenessViolation;
import com.wallet.purchase_ms.dto.PurchaseDto;
import com.wallet.purchase_ms.dto.UpdateBalanceDto;
import com.wallet.purchase_ms.dto.UpdateBalanceType;
import com.wallet.purchase_ms.repository.PurchaseRepository;

import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class PurchaseService implements PurchaseServiceInterface {

    @Autowired
    private InBoxServiceInterface inBoxService;

    @Autowired
    private OutBoxServiceInterface outBoxService;

    @Autowired
    private BalanceServiceInterface balanceService;

    @Autowired
    private PurchaseRepository repository;

    @Autowired
    private TransactionMessageProducer producer;

    @Override
    public void processPendings() {

        List<InBox> pendings = inBoxService.listPending();

        for (InBox inBox : pendings) {

            Purchase purchase = inBoxService.parsePayload(inBox.getPayload(), Purchase.class);

            try {
                log.info("PROCESSING purchase: {}", purchase.getEventId());
                this.process(purchase);
                log.info("PROCESSED purchase: {}", purchase.getEventId());

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

            PurchaseDto dto = outBoxService.parsePayload(outBox.getPayload(), PurchaseDto.class);
            producer.sendPurchase(dto, outBox.getId());
        }
    }

    @Override
    @Transactional(rollbackOn = UniquenessViolation.class)
    public UUID process(Purchase purchase) throws UniquenessViolation {

        this.validateIdempotency(purchase);
        this.validateDuplicity(purchase);

        // Salva a operação
        repository.save(purchase);

        // Armazena a mensagem na OutBox para enviar posteriormente
        outBoxService.save(purchase);

        sendUpdateBalance(purchase);

        log.info("purchase Processed successfully: {}", purchase.getEventId());

        return purchase.getEventId();
    }

    private void sendUpdateBalance(Purchase purchase) {
        // Integra com o serviço que controla o saldo
        // Ao enviar para Balance trabalhar com concorrrência
        UpdateBalanceDto dto = new UpdateBalanceDto(purchase.getReceiverId(), UpdateBalanceType.DEBIT,
        purchase.getValue());

        try {
            balanceService.sendBalanceUpdate(dto, dto.getReceiverId());
        } catch (Exception e) {

            log.error("ERROR ACCESSING BALANCE!");
            throw new RuntimeException();
        }
    }

    // Verifica se uma transação está duplicada dentro do período estabelecido
    private void validateDuplicity(Purchase purchase) throws UniquenessViolation {

        final int DUPLICITY_PERIOD = 5;

        var existent = repository.findBySourceIdAndPayerIdAndReceiverIdAndValue(purchase.getSourceId(),
        purchase.getPayerId(), purchase.getReceiverId(), purchase.getValue());

        if (existent.isPresent()) {
            Duration duration = Duration.between(purchase.getDate().toInstant(), existent.get().getDate().toInstant());
            long diff = Math.abs(duration.toMinutes());

            if (diff < DUPLICITY_PERIOD) {
                log.error("purchase {} violates Idempotency rules. Discarded.", purchase.getEventId());
                throw new UniquenessViolation();
            }
        }
    }

    private void validateIdempotency(Purchase purchase) throws UniquenessViolation {

        Optional<Purchase> existent = repository.findById(purchase.getEventId());
        if (existent.isPresent()) {

            log.error("purchase {} violates Idempotency rules. Discarded.", purchase.getEventId());
            throw new UniquenessViolation();
        }
    }

}
