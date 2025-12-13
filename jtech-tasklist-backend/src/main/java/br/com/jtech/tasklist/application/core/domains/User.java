/*
*  @(#)User.java
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
package br.com.jtech.tasklist.application.core.domains;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import br.com.jtech.tasklist.adapters.input.protocols.UserRequest;
import br.com.jtech.tasklist.adapters.output.repositories.entities.UserEntity;
import br.com.jtech.tasklist.adapters.output.repositories.entities.roles.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
* class User 
* 
* user angelo.vicente 
*/
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User {
	
	private UUID id;
    private String name;
    private String email;
    private String password;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<String> roles;
	private String userType;
	
	public UserEntity toEntity(String encodedPassword) {
		return UserEntity.builder()
				.id(this.id)
				.name(this.getName())
				.email(this.getEmail())
				.password(encodedPassword)
				.roles(getUserRoles(this.getRoles()))
				.enabled(true)
				.createdAt(this.createdAt != null ? this.createdAt : LocalDateTime.now())
                .updateAt(this.updatedAt != null ? this.updatedAt : LocalDateTime.now())
                .build();				
	}
	
    public UserEntity toEntity() {
        return UserEntity.builder()
                .id(this.id)
                .name(this.name)
                .email(this.email)
                .password(this.password)
                .createdAt(this.createdAt != null ? this.createdAt : LocalDateTime.now())
                .updateAt(this.updatedAt != null ? this.updatedAt : LocalDateTime.now())
                .build();
    }
    
    public static User of(UserEntity entity) {
        return User.builder()
                .id(entity.getId())
                .name(entity.getName())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .roles(
                    entity.getRoles() != null
                        ? entity.getRoles().stream().map(Enum::name).collect(Collectors.toList())
                        : List.of()
                )
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdateAt())
                .build();
    }

    public static User of(UserRequest request) {
        return User.builder()
                .id(request.getId())
                .name(request.getName())
                .email(request.getEmail())
                .password(request.getPassword())
                .build();
    }
    
    private Set<Role> getUserRoles(List<String> roles) {
        if (roles != null && !roles.isEmpty()) {
            return roles.stream()
                    .map(Role::valueOf)
                    .collect(Collectors.toSet());
        } else {
            return Collections.singleton(Role.USER);
        }
    }

}
