package com.wallet.statement_ms.services;

import java.util.List;

import com.wallet.statement_ms.domain.InBox;


public interface InBoxServiceInterface {
    public long save(Object payload);
    public void complete(long id);
    public List<InBox> listPending();
    public <T> T parsePayload(String payload, Class<T> classType);
}
