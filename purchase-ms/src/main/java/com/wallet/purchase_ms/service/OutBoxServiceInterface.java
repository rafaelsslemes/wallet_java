package com.wallet.purchase_ms.service;

import java.util.List;

import com.wallet.purchase_ms.domain.OutBox;

public interface OutBoxServiceInterface {
    public long save(Object payload);
    public void complete(long id);
    public List<OutBox> listPending();
    public <T> T parsePayload(String payload, Class<T> classType);
}
