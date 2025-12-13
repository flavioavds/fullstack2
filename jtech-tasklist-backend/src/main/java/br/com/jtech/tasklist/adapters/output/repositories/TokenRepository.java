/*
*  @(#)TokenRepository.java
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

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.jtech.tasklist.adapters.output.repositories.entities.TokenEntity;

/**
* class TokenRepository 
* 
* @author flavio.augusto
*/
public interface TokenRepository extends JpaRepository<TokenEntity, Integer>{
	
	@Query("""
		       select t from TokenEntity t 
		       inner join t.user u 
		       where u.id = :id 
		         and (t.expired = false or t.revoked = false)
		       """)
	List<TokenEntity> findAllValidTokenByUser(@org.springframework.data.repository.query.Param("id") UUID id);


	Optional<TokenEntity> findByToken(String token);
	
}
