package br.com.jtech.tasklist.config.infra.exceptions;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SuccessResponse {
    private String status;
    private String message;
    private LocalDateTime timestamp;
}