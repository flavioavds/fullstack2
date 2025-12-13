/*
*  @(#)AuthAdapter.java
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
package br.com.jtech.tasklist.adapters.output.auth;

import org.springframework.stereotype.Component;

import br.com.jtech.tasklist.adapters.output.repositories.UserRepository;
import br.com.jtech.tasklist.adapters.output.repositories.entities.UserEntity;
import br.com.jtech.tasklist.application.core.domains.User;
import br.com.jtech.tasklist.application.ports.output.auth.RegisterUserOutputGateway;
import lombok.RequiredArgsConstructor;

/**
* class AuthAdapter 
* 
* user flavio.augusto  
*/
@Component
@RequiredArgsConstructor
public class RegisterUserAdapter implements RegisterUserOutputGateway {

    private final UserRepository userRepository;

    @Override
    public User save(User user) {

        UserEntity entity = user.toEntity(user.getPassword());

        UserEntity saved = userRepository.save(entity);

        return User.of(saved);
    }

}