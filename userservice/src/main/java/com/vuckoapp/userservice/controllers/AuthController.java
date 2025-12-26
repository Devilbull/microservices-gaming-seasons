package com.vuckoapp.userservice.controllers;

import com.vuckoapp.userservice.dto.LoginRequest;
import com.vuckoapp.userservice.dto.PasswordForgetRequest;
import com.vuckoapp.userservice.dto.RegisterRequest;
import com.vuckoapp.userservice.dto.ResetPasswordRequest;
import com.vuckoapp.userservice.exceptions.NotFoundException;
import com.vuckoapp.userservice.feigncalls.UserCallsToNotificationserviceService;
import com.vuckoapp.userservice.services.AuthenticationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService service;
    private  final UserCallsToNotificationserviceService notificationservice;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        String token = service.register(request);

        // TODO: Pošalji email korisniku sa linkom za aktivaciju:
        // http://localhost:8080/auth/activate?token=<token>
        // ili ako imaš frontend, http://myfrontend.com/activate?token=<token>

        return ResponseEntity.ok(Map.of("message", "Registered successfully"));
    }

    @GetMapping("/activate")
    public ResponseEntity<?> activate(@RequestParam String token) {
        service.activateUser(token);
        return ResponseEntity.ok(Map.of("message", "Account activated successfully"));
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req,
                                   HttpServletResponse response) {

        String token = service.login(req);

        Cookie cookie = new Cookie("jwt", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // set TRUE if using HTTPS
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60); // 1 day

        response.addCookie(cookie);

        return ResponseEntity.ok(Map.of("message", "Logged in"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {

        Cookie cookie = new Cookie("jwt", "");
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(0);   // Expire the cookie immediately

        response.addCookie(cookie);
       // notificationservice.test();
        return ResponseEntity.ok(Map.of("message", "Logged out"));
    }

    @PostMapping("/password-forget")
    public ResponseEntity<?> passwordForget(@RequestBody PasswordForgetRequest body) {
        service.forgotPassword(body.email());
        return  ResponseEntity.ok(Map.of("message", "Email with token sent successfully"));
    }

    @PostMapping("/password-reset")
    public ResponseEntity<?> passwordReset(@RequestBody ResetPasswordRequest resetPasswordRequest) {
        service.resetPassword(resetPasswordRequest);
        return  ResponseEntity.ok(Map.of("message", "Password reset successfully"));

    }
    // No need, there is a global handler for 404
//    @RequestMapping(value = "/**")
//    @ResponseStatus(code = org.springframework.http.HttpStatus.NOT_FOUND)
//    public void handleUnknownAdminRoutes() {
//
//        throw new NotFoundException();
//    }


}
