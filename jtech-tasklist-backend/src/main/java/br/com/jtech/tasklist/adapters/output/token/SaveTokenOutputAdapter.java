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

import org.springframework.stereotype.Component;

import br.com.jtech.tasklist.adapters.output.repositories.TokenRepository;
import br.com.jtech.tasklist.adapters.output.repositories.UserRepository;
import br.com.jtech.tasklist.application.core.domains.Token;
import br.com.jtech.tasklist.application.ports.output.token.SaveTokenOutputGateway;
import lombok.RequiredArgsConstructor;

/**
* class TokenAdapter 
* 
* user angelo.vicente  
*/
@Component
@RequiredArgsConstructor
public class SaveTokenOutputAdapter implements SaveTokenOutputGateway {

    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;

    @Override
    public void save(Token token) {
        var user = userRepository.findById(token.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        var tokenEntity = token.toEntity(user);
        tokenRepository.save(tokenEntity);
    }
}