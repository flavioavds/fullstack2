/*
*  @(#)TasklistAdapter.java
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
package br.com.jtech.tasklist.adapters.output.tasklist;

import java.util.Optional;

import org.springframework.stereotype.Component;

import br.com.jtech.tasklist.adapters.output.repositories.TasklistRepository;
import br.com.jtech.tasklist.adapters.output.repositories.UserRepository;
import br.com.jtech.tasklist.adapters.output.repositories.entities.TasklistEntity;
import br.com.jtech.tasklist.application.core.domains.Tasklist;
import br.com.jtech.tasklist.application.core.domains.User;
import br.com.jtech.tasklist.application.ports.output.tasklist.CreateTasklistOutputGateway;
import lombok.RequiredArgsConstructor;

/**
* class TasklistAdapter 
* 
* user angelo.vicente  
*/
@Component
@RequiredArgsConstructor
public class CreateTasklistAdapter implements CreateTasklistOutputGateway {

    private final TasklistRepository repository;
    private final UserRepository userRepository;

    @Override
    public Tasklist create(Tasklist tasklist) {
    	TasklistEntity entity = tasklist.toEntity();
    	TasklistEntity savedEntity = repository.save(entity);
          return Tasklist.of(savedEntity);
    }
    
    @Override
	public Optional<User> findByEmail(String email) {
	    return userRepository.findByEmail(email)
	                     .map(User::of);
	}

}