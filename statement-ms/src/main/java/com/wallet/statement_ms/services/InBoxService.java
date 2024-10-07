package com.wallet.statement_ms.services;


import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wallet.statement_ms.domain.InBox;
import com.wallet.statement_ms.repository.repository.InBoxRepository;


@Service
public class InBoxService implements InBoxServiceInterface{

    @Autowired
    private InBoxRepository repository;

    @Override
    public long save(Object payload) {

        InBox newMessage = new InBox();
        String parsed = JsonUtil.parseObjectToJsonString(payload);
        newMessage.setPayload(parsed);

        var saved = repository.save(newMessage);
        return saved.getId();
    }

    @Override
    public void complete(long id) {
        
        InBox inBox = repository.findById(id).get();
        inBox.setProcessed(true);
        repository.save(inBox);
    }

    @Override
    public List<InBox> listPending() {
        List<InBox> pendings = repository.findByProcessed(false);
        return pendings;
    }
    
    @Override
    public <T> T parsePayload(String payload, Class<T> classType) {
    
        T parsed = JsonUtil.parseJsonToObject(payload, classType);
        return parsed;
    }
}
