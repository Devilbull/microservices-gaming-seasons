package com.vuckoapp.userservice.controllers;

import com.vuckoapp.userservice.dto.UserDto;
import com.vuckoapp.userservice.services.UserService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor()
public class AdminController {

    private final UserService userService;



    @GetMapping("/users")
    public List<UserDto> allUsers() {
        return userService.all();
    }

    @PutMapping("/users/{id}/block")
    public void blockUser(@PathVariable UUID id) {
        userService.blockUser(id);
    }

    @PutMapping("/users/{id}/unblock")
    public void unblockUser(@PathVariable UUID id) {
        userService.unblockUser(id);
    }
}

