package com.wallet.cashout_ms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wallet.cashout_ms.domain.OutBox;
import com.wallet.cashout_ms.repository.OutBoxRepository;

@Service
public class OutBoxService implements OutBoxServiceInterface{

    @Autowired
    private OutBoxRepository repository;

    @Override
    public long save(Object payload) {

        OutBox newMessage = new OutBox();
        String parsed = JsonUtil.parseObjectToJsonString(payload);
        newMessage.setPayload(parsed);

        var saved = repository.save(newMessage);
        return saved.getId();
    }

    @Override
    public void complete(long id) {
        
        OutBox outBox = repository.findById(id).get();
        outBox.setProcessed(true);
        repository.save(outBox);
    }

    @Override
    public List<OutBox> listPending() {
        List<OutBox> pendings = repository.findByProcessed(false);
        return pendings;
    }
    
    @Override
    public <T> T parsePayload(String payload, Class<T> classType) {
    
        T parsed = JsonUtil.parseJsonToObject(payload, classType);
        return parsed;
    }
}
