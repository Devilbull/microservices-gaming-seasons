package com.vuckoapp.userservice.controllers;

import com.vuckoapp.userservice.dto.*;
import com.vuckoapp.userservice.model.Role;
import com.vuckoapp.userservice.model.User;
import com.vuckoapp.userservice.model.UserStatus;
import com.vuckoapp.userservice.services.UserService; // servis koji pozivaš
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;                // Lombok anotacija
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication; // za pristup ulogovanom korisniku
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;    // @RestController, @RequestMapping, @GetMapping, @PutMapping

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public UserDto getMe(@AuthenticationPrincipal JwtUserPrincipal principal) {

        return userService.getByUsername(principal.username());
    }

    @GetMapping("/session-eligibility")
    public SessionEligibilityDtoGamingService canCreateSession(@AuthenticationPrincipal JwtUserPrincipal principal) throws InterruptedException {
        //Thread.sleep(10000);

        if(principal.role().equals(Role.ADMIN.toString())){
            return new SessionEligibilityDtoGamingService(true,true);
        }

        UserDto user = userService.getByUsername(principal.username());

        boolean blocked = Objects.equals(user.status(), UserStatus.BLOCKED.toString());
        boolean attendanceOk = true;
        if(!user.role().equals(Role.ADMIN.toString())) {
            attendanceOk = user.gamerStats().attendanceNumber().compareTo( BigDecimal.valueOf(90.0)) >= 0;
        }


        return new SessionEligibilityDtoGamingService(
                blocked,
                attendanceOk
        );
    }

    @PutMapping("/me")
    public UserDto updateMe(@AuthenticationPrincipal JwtUserPrincipal principal, @RequestBody UpdateUserRequest dto) {
        return userService.updateUser(principal.username(), dto);
    }

    @PatchMapping("/me/password")
    public ResponseEntity<?> changePassword(@AuthenticationPrincipal JwtUserPrincipal principal, @RequestBody ChangePasswordRequest req, HttpServletResponse response) {


            userService.changePassword(principal.username(), req);

            Cookie cookie = new Cookie("jwt", "");
            cookie.setHttpOnly(true);
            cookie.setSecure(false);
            cookie.setPath("/");
            cookie.setMaxAge(0);
            response.addCookie(cookie);

            return ResponseEntity.ok(Map.of("message", "Password changed successfully"));
    }


    @GetMapping
    public Page<UserDto> allUsers(
            @RequestParam(required = false) String username,
            @PageableDefault(size = 10, sort = "username") Pageable pageable
    ) {
        return userService.all(username, pageable);
    }
}

