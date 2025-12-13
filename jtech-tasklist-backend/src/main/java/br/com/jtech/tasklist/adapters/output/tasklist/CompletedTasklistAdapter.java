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
import java.util.UUID;

import org.springframework.stereotype.Component;

import br.com.jtech.tasklist.adapters.output.repositories.TasklistRepository;
import br.com.jtech.tasklist.adapters.output.repositories.UserRepository;
import br.com.jtech.tasklist.adapters.output.repositories.entities.TasklistEntity;
import br.com.jtech.tasklist.application.core.domains.Tasklist;
import br.com.jtech.tasklist.application.core.domains.User;
import br.com.jtech.tasklist.application.ports.output.tasklist.CompletedTasklistOutputGateway;
import lombok.RequiredArgsConstructor;

/**
* class TasklistAdapter 
* 
* user flavio.augusto  
*/
@Component
@RequiredArgsConstructor
public class CompletedTasklistAdapter implements CompletedTasklistOutputGateway {

    private final TasklistRepository tasklistRepository;
    private final UserRepository userRepository;

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email).map(User::of);
    }

    @Override
    public Optional<Tasklist> findByIdAndUserId(UUID tasklistId, UUID userId) {
        return tasklistRepository.findById(tasklistId)
                .filter(t -> t.getUser().getId().equals(userId))
                .map(Tasklist::of);
    }

    @Override
    public Tasklist update(Tasklist tasklist) {
        TasklistEntity entity = tasklistRepository.findById(tasklist.getId())
                .orElseThrow(() -> new RuntimeException("Tasklist não encontrada"));
        entity.setCompleted(tasklist.isCompleted());
        tasklistRepository.save(entity);
        return Tasklist.of(entity);
    }
}