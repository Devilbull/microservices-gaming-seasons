package com.vuckoapp.gamingservice.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Map;

public class ResponseBuilder {

    public static ResponseEntity<Object> build(HttpStatus status, String message) {
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
}
