package com.wallet.account_ms.service;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.wallet.account_ms.domain.User;
import com.wallet.account_ms.repository.UserRepository;

@Service
public class UserService implements UserServiceInterface {

    @Autowired
    private UserRepository repository;

    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public User create(User user) throws BadRequestException {
        User existent = repository.findByUsername(user.getUsername());

        if (existent != null){
            throw new BadRequestException();
        }

        user.setPassword(passwordEncoder().encode(user.getPassword()));
        User created = repository.save(user);

        return created;
    }
    
}
