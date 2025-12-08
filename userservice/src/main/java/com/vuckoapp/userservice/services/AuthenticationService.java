package com.vuckoapp.userservice.services;

import com.vuckoapp.userservice.model.*;
import com.vuckoapp.userservice.repository.UserRepository;
import com.vuckoapp.userservice.security.jwt.JwtUtil;
import com.vuckoapp.userservice.services.mapper.RegisterRequestMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RegisterRequestMapper requestMapper;

    public void register(RegisterRequest request) {
        User user = requestMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.password()));

        userRepository.save(user);
    }

    public String login(LoginRequest request) {
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new RuntimeException("Invalid username"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return jwtUtil.generateToken(user);
    }
}

