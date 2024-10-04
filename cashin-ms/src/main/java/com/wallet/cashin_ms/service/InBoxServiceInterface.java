package com.wallet.cashin_ms.service;

import java.util.List;

import com.wallet.cashin_ms.domain.InBox;

public interface InBoxServiceInterface {
    public long save(Object payload);
    public void complete(long id);
    public List<InBox> listPending();
    public <T> T parsePayload(String payload, Class<T> classType);
}
