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
package br.com.jtech.tasklist.adapters.output.repositories.entities;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.hibernate.annotations.UuidGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.jtech.tasklist.adapters.output.repositories.entities.roles.Role;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderColumn;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * class UserEntity
 * 
 * @author flavio.augusto
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "tb_users")
public class UserEntity implements UserDetails{
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue
    @UuidGenerator
    @Column(columnDefinition = "uuid", updatable = false)
    private UUID id;
	
	@NotBlank(message = "Nome não pode estar vazio")
	@Size(max = 150, message = "Nome deve ter no máximo 150 caracteres")
	@Column(nullable = false)
	private String name;
	
	@NotBlank(message = "Email não pode estar vazio")
	@Size(max = 150, message = "Email deve ter no máximo 150 caracteres")
	@Email(message = "Email inválido")
	@Column(nullable = false, unique = true)
	private String email;
	
	@NotBlank(message = "Senha não pode estar vazio")
	@Column(nullable = false)
	private String password;
	
	private boolean enabled;
	
	@Enumerated(EnumType.STRING)
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "tb_users_roles")
	@OrderColumn(name = "role_index")
	@Column(name = "role")
	@Builder.Default
	private Set<Role> roles = new HashSet<>();
	
	@JsonIgnore
	@Builder.Default
	private boolean deleted = Boolean.FALSE;
		
	@Builder.Default
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt = LocalDateTime.now();
	
	@Builder.Default
	@Column(nullable = false)
	private LocalDateTime updateAt = LocalDateTime.now();
	
	@OneToMany(mappedBy = "user")
	private List<TasklistEntity> tasklists;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getRole())).collect(Collectors.toList());
	}

	@Override
	public String getUsername() {
		return email;
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	
	@Override
	public boolean isAccountNonLocked() {
	    return enabled;
	}
	
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

}
