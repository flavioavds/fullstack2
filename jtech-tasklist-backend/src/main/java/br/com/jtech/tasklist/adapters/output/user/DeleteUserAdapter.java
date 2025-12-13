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
import br.com.jtech.tasklist.application.ports.output.user.DeleteUserOutputGateway;
import lombok.RequiredArgsConstructor;

/**
* class UserAdapter 
* 
* user flavio.augusto  
*/
@Component
@RequiredArgsConstructor
public class DeleteUserAdapter implements DeleteUserOutputGateway {
    private final UserRepository repository;

    @Override
    public void delete(UUID id) {
        repository.deleteById(id);
    }
    
    @Override
	public Optional<User> findByEmail(String email) {
	    return repository.findByEmail(email)
	                     .map(User::of);
	}
}