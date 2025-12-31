package com.vuckoapp.userservice.controllers;


import com.vuckoapp.userservice.dto.JwtUserPrincipal;
import com.vuckoapp.userservice.dto.UserDto;
import com.vuckoapp.userservice.services.GameServiceRequestsService;
import com.vuckoapp.userservice.services.UserService;
import com.vuckoapp.userservice.utils.ResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/gameservice")
@RequiredArgsConstructor
public class GameserviceRequestsController {
    private final GameServiceRequestsService gameServiceRequestsService;
    private final UserService userService;

    @PostMapping("/{userId}/update-total-sessions")
    public ResponseEntity<?> increaseNumberOfSeasonsJoined(@PathVariable UUID userId,@AuthenticationPrincipal JwtUserPrincipal principal) {
        if(principal.role().equals("ADMIN")){
            return ResponseBuilder.build(HttpStatus.FORBIDDEN,"Admins are not allowed to perform this action");
        }
        return gameServiceRequestsService.increaseNumberOfSeasonsJoined(userId);
    }

    @PostMapping("/{userId}/user-info")
    public UserDto getUserById(@PathVariable UUID userId) {

        return userService.getById(userId);
    }
}
