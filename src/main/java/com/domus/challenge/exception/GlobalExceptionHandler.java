package com.domus.challenge.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebInputException;

import java.time.Instant;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handle(Exception ex, ServerHttpRequest request) {
        log.error("Exception occurred", ex);

        ApiError error = switch (ex) {
            case ServerWebInputException e -> buildError(HttpStatus.BAD_REQUEST, "The threshold parameter is missing or must be a numeric value with out decimals.", request);
            case ConstraintViolationException e -> buildError(HttpStatus.BAD_REQUEST, "Validation failed", request);
            case MovieApiException e -> buildError(HttpStatus.BAD_GATEWAY, e.getMessage(), request);
            case ValidationException e -> buildError(HttpStatus.BAD_REQUEST, e.getMessage(), request);
            default -> buildError(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred", request);
        };

        return ResponseEntity.status(error.status()).body(error);
    }

    private ApiError buildError(HttpStatus status, String message, ServerHttpRequest request) {
        return new ApiError(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getPath().value()
        );
    }
}
