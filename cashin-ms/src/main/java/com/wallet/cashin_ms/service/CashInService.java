package com.wallet.cashin_ms.service;

import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.wallet.cashin_ms.controller.TransactionMessageProducer;
import com.wallet.cashin_ms.domain.CashIn;
import com.wallet.cashin_ms.domain.InBox;
import com.wallet.cashin_ms.domain.OutBox;
import com.wallet.cashin_ms.domain.errors.UniquenessViolation;
import com.wallet.cashin_ms.dto.CashInDto;
import com.wallet.cashin_ms.dto.UpdateBalanceDto;
import com.wallet.cashin_ms.dto.UpdateBalanceType;
import com.wallet.cashin_ms.repository.CashInRepository;

import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class CashInService implements CashInServiceInterface {

    @Autowired
    private InBoxServiceInterface inBoxService;

    @Autowired
    private OutBoxServiceInterface outBoxService;

    @Autowired
    private BalanceServiceInterface balanceService;

    @Autowired
    private CashInRepository repository;

    @Autowired
    private TransactionMessageProducer producer;

    @Override
    public void processPendings() {

        List<InBox> pendings = inBoxService.listPending();

        for (InBox inBox : pendings) {

            CashIn cashIn = inBoxService.parsePayload(inBox.getPayload(), CashIn.class);

            try {
                this.process(cashIn);
                inBoxService.complete(inBox.getId());
            } catch (Exception e) {
                continue;
            }
        }
    }

    @Override
    public void sendProcessed() {

        List<OutBox> pendings = outBoxService.listPending();

        for (OutBox outBox : pendings) {

            CashInDto dto = outBoxService.parsePayload(outBox.getPayload(), CashInDto.class);
            producer.sendCashIn(dto, outBox.getId());
        }
    }

    @Override
    @Transactional
    public UUID process(CashIn cashIn) throws UniquenessViolation {

        this.validateIdempotency(cashIn);
        this.validateDuplicity(cashIn);

        // Salva a operação
        repository.save(cashIn);

        // Armazena a mensagem na OutBox para enviar posteriormente
        outBoxService.save(cashIn);

        // Integra com o serviço que controla o saldo
        // Ao enviar para Balance trabalhar com concorrrência
        UpdateBalanceDto dto = new UpdateBalanceDto(cashIn.getReceiverId(), UpdateBalanceType.CREDIT,
                cashIn.getValue());
        ResponseEntity response;
        response = balanceService.sendBalanceUpdate(dto, dto.getReceiverId());
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException();
        }

        log.info("CashIn Processed successfully: {}", cashIn.getEventId());

        return cashIn.getEventId();
    }

    // Verifica se uma transação está duplicada dentro do período estabelecido
    private void validateDuplicity(CashIn cashIn) throws UniquenessViolation {

        final int DUPLICITY_PERIOD = 5;

        var existent = repository.findBySourceIdAndPayerIdAndReceiverIdAndValue(cashIn.getSourceId(),
                cashIn.getPayerId(), cashIn.getReceiverId(), cashIn.getValue());

        if (existent.isPresent()) {
            Duration duration = Duration.between(cashIn.getDate().toInstant(), existent.get().getDate().toInstant());
            long diff = Math.abs(duration.toMinutes());

            if (diff < DUPLICITY_PERIOD) {
                log.error("CashIn {} violates Idempotency rules. Discarded.", cashIn.getEventId());
                throw new UniquenessViolation();
            }
        }
    }

    private void validateIdempotency(CashIn cashIn) throws UniquenessViolation {

        var existent = repository.findById(cashIn.getEventId());
        if (existent.isPresent()) {

            log.error("CashIn {} violates Idempotency rules. Discarded.", cashIn.getEventId());
            throw new UniquenessViolation();
        }
    }

}
