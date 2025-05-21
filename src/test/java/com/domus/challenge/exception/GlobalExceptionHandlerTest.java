package com.domus.challenge.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebInputException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;
    private ServerHttpRequest mockRequest;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
        mockRequest = mock(ServerHttpRequest.class);
        RequestPath mockPath = mock(RequestPath.class);
        when(mockPath.value()).thenReturn("/api/directors");
        when(mockRequest.getPath()).thenReturn(mockPath);
    }

    @Test
    void handleServerWebInputException_returnsBadRequest() {
        var ex = new ServerWebInputException("The threshold parameter is missing or must be a numeric value with out decimals.");
        var response = handler.handle(ex, mockRequest);

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().status());
        assertEquals("The threshold parameter is missing or must be a numeric value with out decimals.", response.getBody().message());
    }


    @Test
    void handleGenericException_returnsInternalServerError() {
        var ex = new RuntimeException("Unexpected crash");
        var response = handler.handle(ex, mockRequest);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getBody().status());
        assertEquals("Unexpected error occurred", response.getBody().message());
    }
}
