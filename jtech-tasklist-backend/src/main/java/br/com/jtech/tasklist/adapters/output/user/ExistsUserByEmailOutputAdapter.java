/*
*  @(#)UserAdapter.java
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
package br.com.jtech.tasklist.adapters.output.user;

import org.springframework.stereotype.Component;

import br.com.jtech.tasklist.adapters.output.repositories.UserRepository;
import br.com.jtech.tasklist.application.ports.output.user.ExistsUserByEmailOutputGateway;
import lombok.RequiredArgsConstructor;

/**
* class UserAdapter 
* 
* user flavio.augusto  
*/
@Component
@RequiredArgsConstructor
public class ExistsUserByEmailOutputAdapter implements ExistsUserByEmailOutputGateway {

    private final UserRepository userRepository;

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email).isPresent();
    }
}