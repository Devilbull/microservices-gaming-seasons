package com.vuckoapp.userservice.controllers;

import com.vuckoapp.userservice.dto.UpdateUserRequest; // DTO za update
import com.vuckoapp.userservice.dto.UserDto;          // DTO za prikaz korisnika
import com.vuckoapp.userservice.services.UserService; // servis koji pozivaš
import lombok.RequiredArgsConstructor;                // Lombok anotacija
import org.springframework.security.core.Authentication; // za pristup ulogovanom korisniku
import org.springframework.web.bind.annotation.*;    // @RestController, @RequestMapping, @GetMapping, @PutMapping

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public UserDto getMe(Authentication auth) {
        return userService.getByUsername(auth.getName());
    }

    @PutMapping("/me")
    public UserDto updateMe(Authentication auth, @RequestBody UpdateUserRequest dto) {
        return userService.updateUser(auth.getName(), dto);
    }
}

