/*
*  @(#)AuthUseCase.java
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
package br.com.jtech.tasklist.application.core.usecases.auth;

import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.jtech.tasklist.adapters.output.repositories.entities.TokenType;
import br.com.jtech.tasklist.application.core.domains.Token;
import br.com.jtech.tasklist.application.core.domains.User;
import br.com.jtech.tasklist.application.ports.input.auth.RegisterUserInputGateway;
import br.com.jtech.tasklist.application.ports.output.auth.RegisterUserOutputGateway;
import br.com.jtech.tasklist.application.ports.output.token.JwtTokenOutputGateway;
import br.com.jtech.tasklist.application.ports.output.token.SaveTokenOutputGateway;
import br.com.jtech.tasklist.application.ports.output.user.ExistsUserByEmailOutputGateway;

/**
* class AuthUseCase  
* 
* user flavio.augusto  
*/
public class RegisterUserUserCase implements RegisterUserInputGateway{
	
	private final ExistsUserByEmailOutputGateway existsUserByEmailOutputGateway;
    private final RegisterUserOutputGateway registerUserOutputGateway;
    private final JwtTokenOutputGateway jwtTokenOutputGateway;
    private final SaveTokenOutputGateway saveTokenOutputGateway;
    private final PasswordEncoder passwordEncoder;

	public RegisterUserUserCase(ExistsUserByEmailOutputGateway existsUserByEmailOutputGateway,
			RegisterUserOutputGateway registerUserOutputGateway, JwtTokenOutputGateway jwtTokenOutputGateway,
			SaveTokenOutputGateway saveTokenOutputGateway, PasswordEncoder passwordEncoder) {
		this.existsUserByEmailOutputGateway = existsUserByEmailOutputGateway;
		this.registerUserOutputGateway = registerUserOutputGateway;
		this.jwtTokenOutputGateway = jwtTokenOutputGateway;
		this.saveTokenOutputGateway = saveTokenOutputGateway;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public User register(User user) {

	    if (existsUserByEmailOutputGateway.existsByEmail(user.getEmail())) {
	        throw new IllegalArgumentException("E-mail já registrado");
	    }

	    user.setPassword(passwordEncoder.encode(user.getPassword()));

	    User savedUser = registerUserOutputGateway.save(user);

	    String jwtToken = jwtTokenOutputGateway.generateToken(savedUser);

	    Token token = Token.builder()
	            .token(jwtToken)
	            .tokenType(TokenType.BEARER)
	            .expired(false)
	            .revoked(false)
	            .userId(savedUser.getId())
	            .build();

	    saveTokenOutputGateway.save(token);

	    return savedUser;
	}

}
