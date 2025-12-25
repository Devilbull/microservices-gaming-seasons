package com.vuckoapp.gamingservice.exceptions;

import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalFeignExceptionHandler {

    private ResponseEntity<Object> build(HttpStatus status, String message) {
        return new ResponseEntity<>(
                Map.of(
                        "timestamp", LocalDateTime.now().toString(),
                        "status", status.value(),
                        "error", status.getReasonPhrase(),
                        "message", message
                ),
                status
        );
    }

    @ExceptionHandler(FeignException.Unauthorized.class)
    public ResponseEntity<?> handle401(FeignException.Unauthorized ex) {
        return build(HttpStatus.UNAUTHORIZED, "Unauthorized");
    }

    @ExceptionHandler(FeignException.Forbidden.class)
    public ResponseEntity<?> handle403(FeignException.Forbidden ex) {
        return build(HttpStatus.FORBIDDEN, "Forbidden");
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<?> handleFeign(FeignException ex) {
        return build(HttpStatus.SERVICE_UNAVAILABLE, "Feign client error: " + ex.getMessage());
    }
}
