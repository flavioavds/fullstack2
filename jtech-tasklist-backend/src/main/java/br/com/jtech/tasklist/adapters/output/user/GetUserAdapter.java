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

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import br.com.jtech.tasklist.adapters.output.repositories.UserRepository;
import br.com.jtech.tasklist.application.core.domains.User;
import br.com.jtech.tasklist.application.ports.output.user.GetUserOutputGateway;
import lombok.RequiredArgsConstructor;

/**
* class UserAdapter 
* 
* user flavio.augusto  
*/
@Component
@RequiredArgsConstructor
public class GetUserAdapter implements GetUserOutputGateway {
	
    private final UserRepository repository;

    @Override
    public User getById(UUID id) {
        return repository.findById(id).map(User::of).orElse(null);
    }
    
    @Override
	public Optional<User> findByEmail(String email) {
	    return repository.findByEmail(email)
	                     .map(User::of);
	}
}