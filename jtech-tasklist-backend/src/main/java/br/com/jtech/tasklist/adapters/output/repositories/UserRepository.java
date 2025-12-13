/*
*  @(#)UserRepository.java
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
package br.com.jtech.tasklist.adapters.output.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.jtech.tasklist.adapters.output.repositories.entities.UserEntity;

/**
* class UserRepository 
* 
* @author flavio.augusto
*/
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
	
	Optional<UserEntity> findByEmailIgnoreCase(String email);
	Optional<UserEntity> findById(UUID id);
	Optional<UserEntity> findByEmail(String email);
	Optional<UserEntity> findByNameIgnoreCase(String name);

}
