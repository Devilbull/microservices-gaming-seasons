package com.vuckoapp.userservice.controllers;

import com.vuckoapp.userservice.dto.ChangePasswordRequest;
import com.vuckoapp.userservice.dto.UpdateUserRequest; // DTO za update
import com.vuckoapp.userservice.dto.UserDto;          // DTO za prikaz korisnika
import com.vuckoapp.userservice.services.UserService; // servis koji pozivaš
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;                // Lombok anotacija
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication; // za pristup ulogovanom korisniku
import org.springframework.web.bind.annotation.*;    // @RestController, @RequestMapping, @GetMapping, @PutMapping

import java.util.Map;

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

    @PatchMapping("/me/password")
    public ResponseEntity<?> changePassword(Authentication auth, @RequestBody ChangePasswordRequest req, HttpServletResponse response) {

        try{
            userService.changePassword(auth.getName(), req);
            Cookie cookie = new Cookie("jwt", "");
            cookie.setHttpOnly(true);
            cookie.setSecure(false);
            cookie.setPath("/");
            cookie.setMaxAge(0);

            response.addCookie(cookie);

            return ResponseEntity.ok(Map.of("message", "Password changed successfully"));

        } catch(RuntimeException e){
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        }


    }
}

