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

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import br.com.jtech.tasklist.adapters.output.repositories.UserRepository;
import br.com.jtech.tasklist.application.core.domains.User;
import br.com.jtech.tasklist.application.ports.output.user.UpdateUserOutputGateway;
import lombok.RequiredArgsConstructor;

/**
* class UserAdapter 
* 
* user flavio.augusto  
*/
@Component
@RequiredArgsConstructor
public class UpdateUserAdapter implements UpdateUserOutputGateway {
    
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User update(UUID id, User user) {
        return repository.findById(id)
            .map(existing -> {
                existing.setName(user.getName());
                existing.setEmail(user.getEmail());
                
                if (user.getPassword() != null && !user.getPassword().isBlank()) {
                    existing.setPassword(passwordEncoder.encode(user.getPassword()));
                }
                
                existing.setUpdateAt(user.getUpdatedAt() != null ? user.getUpdatedAt() : java.time.LocalDateTime.now());
                return User.of(repository.save(existing));
            }).orElse(null);
    }

	@Override
	public Optional<User> findByEmail(String email) {
	    return repository.findByEmail(email)
	                     .map(User::of);
	}

}