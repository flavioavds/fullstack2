/*
*  @(#)RegisterUserUserCaseConfig.java
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
package br.com.jtech.tasklist.config.usecases.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.jtech.tasklist.application.core.usecases.auth.RegisterUserUserCase;
import br.com.jtech.tasklist.application.ports.output.auth.RegisterUserOutputGateway;
import br.com.jtech.tasklist.application.ports.output.token.JwtTokenOutputGateway;
import br.com.jtech.tasklist.application.ports.output.token.SaveTokenOutputGateway;
import br.com.jtech.tasklist.application.ports.output.user.ExistsUserByEmailOutputGateway;
import lombok.RequiredArgsConstructor;

/**
* class RegisterUserUserCaseConfig
* 
* user flavio.augusto
*/
@Configuration
@RequiredArgsConstructor
public class RegisterUserUserCaseConfig {

    private final ExistsUserByEmailOutputGateway existsUserByEmailOutputGateway;
    private final RegisterUserOutputGateway registerUserOutputGateway;
    private final JwtTokenOutputGateway jwtTokenOutputGateway;
    private final SaveTokenOutputGateway saveTokenOutputGateway;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public RegisterUserUserCase registerUserUserCase() {
        return new RegisterUserUserCase(
                existsUserByEmailOutputGateway,
                registerUserOutputGateway,
                jwtTokenOutputGateway,
                saveTokenOutputGateway,
                passwordEncoder
        );
    }
}