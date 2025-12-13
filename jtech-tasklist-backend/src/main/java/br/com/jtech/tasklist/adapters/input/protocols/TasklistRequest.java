/*
 *  @(#)TasklistRequest.java
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
package br.com.jtech.tasklist.adapters.input.protocols;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;

import br.com.jtech.tasklist.application.core.domains.Tasklist;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* class TasklistRequest 
* 
* user angelo.vicente 
*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TasklistRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	private UUID id;
	
	@NotBlank(message = "Nome do titulo não pode estar vazio")
	private String name;
	
	@NotBlank(message = "Destrição da Tarefa não pode estar vazio")
    private String description;
    private boolean completed;
    private UUID userId;

    private List<TasklistRequest> requests;
    
    public Tasklist toDomain() {
    	return Tasklist.builder()
    			.id(this.id)
    			.name(this.name)
    			.description(this.description)
    			.completed(this.completed)
    			.userId(this.userId)
    			.build();
    }
}