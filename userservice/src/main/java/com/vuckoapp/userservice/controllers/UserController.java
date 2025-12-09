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
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public UserDto getMe(Authentication auth) {
        return userService.getByEmail(auth.getName());
    }

    @PutMapping("/me")
    public UserDto updateMe(Authentication auth, @RequestBody UpdateUserRequest dto) {
        return userService.updateUser(auth.getName(), dto);
    }
}

