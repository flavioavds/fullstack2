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

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import br.com.jtech.tasklist.adapters.output.repositories.TasklistRepository;
import br.com.jtech.tasklist.adapters.output.repositories.UserRepository;
import br.com.jtech.tasklist.adapters.output.repositories.entities.TasklistEntity;
import br.com.jtech.tasklist.application.core.domains.Tasklist;
import br.com.jtech.tasklist.application.core.domains.User;
import br.com.jtech.tasklist.application.ports.output.tasklist.ListTasklistOutputGateway;
import lombok.RequiredArgsConstructor;

/**
* class TasklistAdapter 
* 
* user flavio.augusto  
*/
@Component
@RequiredArgsConstructor
public class ListTasklistAdapter implements ListTasklistOutputGateway{
	
	private final TasklistRepository repository;
    private final UserRepository userRepository;

    @Override
    public List<Tasklist> listAll() {
        List<TasklistEntity> entities = repository.findAll();
        return Tasklist.of(entities);
    }

    @Override
    public List<Tasklist> listAll(UUID userId) {
        List<TasklistEntity> entities = repository.findByUserId(userId);
        return Tasklist.of(entities);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email).map(User::of);
    }

}
