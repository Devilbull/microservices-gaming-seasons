// java
package com.vuckoapp.userservice.controllers;

import com.vuckoapp.userservice.dto.CreateUserRequest;
import com.vuckoapp.userservice.dto.UserDto;
import com.vuckoapp.userservice.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public List<UserDto> all() {
        return service.all();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> byId(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(service.byId(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public UserDto create(@Valid @RequestBody CreateUserRequest req) {
        return service.create(req);
    }
}
