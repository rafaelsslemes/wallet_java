package com.wallet.cashin_ms.service;

import java.util.List;

import org.aspectj.bridge.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wallet.cashin_ms.domain.OutBox;
import com.wallet.cashin_ms.repository.OutBoxRepository;

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
