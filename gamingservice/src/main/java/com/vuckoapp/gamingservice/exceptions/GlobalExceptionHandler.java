package com.vuckoapp.gamingservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

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
    @ExceptionHandler(DownstreamServiceException.class)
    public ResponseEntity<Object> handleDownstream(DownstreamServiceException ex) {
        return build(HttpStatus.SERVICE_UNAVAILABLE, ex.getMessage());
    }

    @ExceptionHandler(feign.FeignException.class)
    public ResponseEntity<Object> handleFeign(feign.FeignException ex) {

        if (ex.status() == 401) return build(HttpStatus.UNAUTHORIZED, "Unauthorized");
        if (ex.status() == 403) return build(HttpStatus.FORBIDDEN, "Forbidden");
        return build(HttpStatus.BAD_GATEWAY, "UserService error: " + ex.getMessage());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Object> handleRouteNotFound(NoHandlerFoundException ex) {
        return build(HttpStatus.NOT_FOUND, "Route not found");
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntime(RuntimeException ex) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
}