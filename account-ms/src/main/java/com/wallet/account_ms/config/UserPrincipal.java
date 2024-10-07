package com.wallet.account_ms.config;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.wallet.account_ms.domain.User;

import lombok.Data;

@Data
public class UserPrincipal {
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    private UserPrincipal(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();

        this.authorities = user.getRoles().stream().map(role -> {
            return new SimpleGrantedAuthority("ROLE_".concat(role.getName()));
        }).collect(Collectors.toList());
    }

    public static UserPrincipal create (User user) {
        return new UserPrincipal(user);
    }

}
