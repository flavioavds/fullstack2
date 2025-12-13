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

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import br.com.jtech.tasklist.adapters.output.repositories.TasklistRepository;
import br.com.jtech.tasklist.adapters.output.repositories.UserRepository;
import br.com.jtech.tasklist.adapters.output.repositories.entities.TasklistEntity;
import br.com.jtech.tasklist.application.core.domains.Tasklist;
import br.com.jtech.tasklist.application.core.domains.User;
import br.com.jtech.tasklist.application.ports.output.tasklist.UpdateTasklistOutputGateway;
import lombok.RequiredArgsConstructor;

/**
* class TasklistAdapter 
* 
* user flavio.augusto  
*/
@Component
@RequiredArgsConstructor
public class UpdateTasklistAdapter implements UpdateTasklistOutputGateway{
	
	private final TasklistRepository repository;
	private final UserRepository userRepository;

    @Override
    public Tasklist update(String id, Tasklist tasklist) {
        return repository.findById(UUID.fromString(id))
                .map(entity -> {
                    entity.setName(tasklist.getName());
                    entity.setDesciption(tasklist.getDescription());
                    entity.setCompleted(tasklist.isCompleted());
                    entity.setUpdatedAt(LocalDateTime.now());
                    TasklistEntity updated = repository.save(entity);
                    return Tasklist.of(updated);
                })
                .orElse(null); 
    }
    
    @Override
	public Optional<User> findByEmail(String email) {
	    return userRepository.findByEmail(email)
	                     .map(User::of);
	}
    
    @Override
    public Optional<Tasklist> findById(String id) {
        return repository.findById(UUID.fromString(id))
                         .map(Tasklist::of);
    }

}
