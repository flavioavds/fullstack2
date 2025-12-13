/*
*  @(#)TokenAdapter.java
*
*  Copyright (c) J-Tech Solucoes em Informatica.
*  All Rights Reserved.
*
*  This software is the confidential and proprietary information of J-Tech.
*  ("Confidential Information"). You shall not disclose such Confidential
*  Information and shall use it only in accordance with the terms of the
*  license agreement you entered into with J-Tech.
*
*/
package br.com.jtech.tasklist.adapters.output.token;

import java.util.Optional;

import org.springframework.stereotype.Component;

import br.com.jtech.tasklist.adapters.output.repositories.TokenRepository;
import br.com.jtech.tasklist.adapters.output.repositories.UserRepository;
import br.com.jtech.tasklist.application.core.domains.User;
import br.com.jtech.tasklist.application.ports.output.token.LogoutOutputGateway;
import lombok.RequiredArgsConstructor;

/**
* class TokenAdapter 
* 
* user angelo.vicente  
*/
@Component
@RequiredArgsConstructor
public class LogoutAdapter implements LogoutOutputGateway {

    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;

    @Override
    public void revokeAllTokens(User user) {
        var tokens = tokenRepository.findAllValidTokenByUser(user.getId());
        tokens.forEach(t -> {
            t.setExpired(true);
            t.setRevoked(true);
        });

        tokenRepository.saveAll(tokens);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email)
                .map(User::of);
    }
}