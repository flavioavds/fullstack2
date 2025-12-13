/*
*  @(#)UserEntity.java
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
package br.com.jtech.tasklist.adapters.output.repositories.entities.roles;

/**
 * class UserEntity
 * 
 * @author flavio.augusto
 */
public enum Role {
	
	USER("ROLE_USER"),
	ADMIN("ROLE_ADMIN");
	
	private String role;
	
	private Role(String role) {
		this.role = role;
	}
	
	public String getRole() {
		return role;
	}

}
