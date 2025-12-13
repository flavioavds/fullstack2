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

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import br.com.jtech.tasklist.adapters.output.repositories.UserRepository;
import br.com.jtech.tasklist.adapters.output.repositories.entities.UserEntity;
import br.com.jtech.tasklist.application.core.domains.User;
import br.com.jtech.tasklist.application.ports.output.user.ListUserOutputGateway;
import lombok.RequiredArgsConstructor;

/**
* class UserAdapter 
* 
* user flavio.augusto  
*/
@Component
@RequiredArgsConstructor
public class ListUserAdapter implements ListUserOutputGateway {
	
    private final UserRepository repository;

    @Override
    public List<User> listAll() {
        List<UserEntity> entities = repository.findAll();
        return entities.stream().map(User::of).toList();
    }
    
    @Override
	public Optional<User> findByEmail(String email) {
	    return repository.findByEmail(email)
	                     .map(User::of);
	}
}