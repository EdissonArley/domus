package com.domus.challenge.exception;

public sealed class ApiException extends RuntimeException
        permits MovieApiException, ValidationException {

    public ApiException(String message) {
        super(message);
    }
}
