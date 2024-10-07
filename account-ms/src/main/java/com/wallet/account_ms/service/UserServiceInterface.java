package com.wallet.account_ms.service;

import org.apache.coyote.BadRequestException;

import com.wallet.account_ms.domain.User;

public interface UserServiceInterface {

    public User create(User user)  throws BadRequestException;
}
