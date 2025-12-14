/*
 * @(#)GlobalExceptionHandler.java
 *
 * Copyright (c) J-Tech Solucoes em Informatica.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of J-Tech.
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with J-Tech.
 */
package br.com.jtech.tasklist.config.infra.handlers;



import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import br.com.jtech.tasklist.config.infra.exceptions.ApiError;
import br.com.jtech.tasklist.config.infra.exceptions.ApiSubError;
import br.com.jtech.tasklist.config.infra.exceptions.ApiValidationError;
import br.com.jtech.tasklist.config.infra.exceptions.JwtAuthenticationException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;

/**
 * Create a global exception handler for intercepting all exceptions in the api.
 *
 * @author angelo.vicente
 * class GlobalExceptionHandler
 **/
@RestControllerAdvice
public class GlobalExceptionHandler {
	
	private ResponseEntity<ApiError> build(HttpStatus status, String message, String debugMessage) {
        ApiError error = new ApiError(status);
        error.setTimestamp(LocalDateTime.now());
        error.setMessage(message);
        error.setDebugMessage(debugMessage);
        return new ResponseEntity<>(error, status);
    }
	
    /**
     * This method handles spring validations.
     *
     * @param ex Exception thrown.
     * @return Return a {@link ApiError} with an array of errors.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationErrors(MethodArgumentNotValidException ex) {
        ApiError error = new ApiError(HttpStatus.BAD_REQUEST);
        error.setMessage("Error on request");
        error.setTimestamp(LocalDateTime.now());
        error.setSubErrors(subErrors(ex));
        error.setDebugMessage(ex.getLocalizedMessage());
        return buildResponseEntity(error);
    }

    private ResponseEntity<ApiError> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }


    private List<ApiSubError> subErrors(MethodArgumentNotValidException ex) {
        List<ApiSubError> errors = new ArrayList<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            ApiValidationError api = new ApiValidationError(ex.getObjectName(), fieldError.getField(), fieldError.getRejectedValue(), fieldError.getDefaultMessage());
            errors.add(api);

        }
        return errors;
    }
    
    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<ApiError> handleTransactionSystemException(TransactionSystemException ex) {
        Throwable cause = ex.getRootCause();
        if (cause instanceof ConstraintViolationException violation) {
        	List<ApiSubError> errors = violation.getConstraintViolations().stream()
        		    .map(cv -> new ApiValidationError(
        		            cv.getRootBeanClass().getSimpleName(),
        		            cv.getPropertyPath().toString(),
        		            cv.getInvalidValue(),
        		            cv.getMessage()))
        		    .collect(Collectors.toCollection(ArrayList::new));

            ApiError error = new ApiError(HttpStatus.BAD_REQUEST);
            error.setMessage("Erro de validação da entidade");
            error.setTimestamp(LocalDateTime.now());
            error.setSubErrors(new ArrayList<>(errors));
            error.setDebugMessage(ex.getLocalizedMessage());

            return ResponseEntity.badRequest().body(error);
        }
        
        return build(HttpStatus.INTERNAL_SERVER_ERROR,
                "Erro inesperado no processamento da requisição",
                ex.getLocalizedMessage());
    }
    /**
     * Violação de constraints (Bean Validation)
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolation(ConstraintViolationException ex) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage(), ex.getLocalizedMessage());
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiError> handleValidation(ValidationException ex) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage(), ex.getLocalizedMessage());
    }

    /**
     * Erros de banco (unicidade, FK, etc.)
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrity(DataIntegrityViolationException ex) {
        return build(HttpStatus.CONFLICT, "Violação de integridade de dados", ex.getMostSpecificCause().getMessage());
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApiError> handleDataAccess(DataAccessException ex) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Erro de acesso a dados", ex.getLocalizedMessage());
    }

    /**
     * Não encontrado
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiError> handleEntityNotFound(EntityNotFoundException ex) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage(), ex.getLocalizedMessage());
    }

    /**
     * Autenticação
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> handleBadCredentials(BadCredentialsException ex) {
        return build(HttpStatus.UNAUTHORIZED, "Credenciais inválidas", ex.getLocalizedMessage());
    }

    @ExceptionHandler(JwtAuthenticationException.class)
    public ResponseEntity<ApiError> handleJwt(JwtAuthenticationException ex) {
        return build(HttpStatus.UNAUTHORIZED, "Token inválido ou expirado", ex.getLocalizedMessage());
    }

    /**
     * Argumentos inválidos
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArg(IllegalArgumentException ex) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage(), ex.getLocalizedMessage());
    }

    /**
     * Erro genérico
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleException(Exception ex) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR,
                "Erro inesperado no processamento da requisição",
                ex.getLocalizedMessage());
    }
    
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiError> handleResponseStatusException(ResponseStatusException ex) {
    	return build(HttpStatus.BAD_REQUEST,
                "Erro inesperado no processamento da requisição",
                ex.getLocalizedMessage());
    }
    
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiError> handleNoResourceFound(NoResourceFoundException ex) {
        ApiError error = new ApiError(HttpStatus.NOT_FOUND);
        error.setTimestamp(LocalDateTime.now());
        error.setMessage("Recurso não encontrado");
        error.setDebugMessage(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}