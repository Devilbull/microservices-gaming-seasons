package com.vuckoapp.userservice.controllers;


import com.vuckoapp.userservice.dto.JwtUserPrincipal;
import com.vuckoapp.userservice.services.GameServiceRequestsService;
import com.vuckoapp.userservice.utils.ResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.protocol.HTTP;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/gameservice")
@RequiredArgsConstructor
public class GameServiceRequestsController {
    private final GameServiceRequestsService gameServiceRequestsService;

    @PostMapping("/{userId}/update-total-sessions")
    public ResponseEntity<?> increaseNumberOfSeasonsJoined(@PathVariable UUID userId,@AuthenticationPrincipal JwtUserPrincipal principal) {
        if(principal.role().equals("ADMIN")){
            return ResponseBuilder.build(HttpStatus.FORBIDDEN,"Admins are not allowed to perform this action");
        }
        return gameServiceRequestsService.increaseNumberOfSeasonsJoined(userId);
    }
}
