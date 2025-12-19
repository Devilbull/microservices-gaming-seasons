package com.vuckoapp.userservice.services;

import com.vuckoapp.userservice.dto.LoginRequest;
import com.vuckoapp.userservice.dto.NotificationRequest;
import com.vuckoapp.userservice.dto.RegisterRequest;
import com.vuckoapp.userservice.dto.ResetPasswordRequest;
import com.vuckoapp.userservice.exceptions.InvalidCredentialsException;
import com.vuckoapp.userservice.exceptions.UserAlreadyExistsException;
import com.vuckoapp.userservice.exceptions.UserBlockedException;
import com.vuckoapp.userservice.exceptions.UserNotActivatedException;
import com.vuckoapp.userservice.exceptions.TokenIsInvalid;
import com.vuckoapp.userservice.messager.NotificationProducer;
import com.vuckoapp.userservice.model.*;
import com.vuckoapp.userservice.repository.ActivationTokenRepository;
import com.vuckoapp.userservice.repository.PasswordResetTokenRepository;
import com.vuckoapp.userservice.repository.UserRepository;
import com.vuckoapp.userservice.security.jwt.JwtUtil;
import com.vuckoapp.userservice.services.mapper.RegisterRequestMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RegisterRequestMapper requestMapper;
    private final ActivationTokenRepository tokenRepository;
    private final NotificationProducer notificationProducer;
    private final PasswordResetTokenRepository passwordResetTokenRepository;


    public String register(RegisterRequest request) {
        if (userRepository.findByUsername(request.username()).isPresent()) {
            throw new UserAlreadyExistsException();
        }

        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new UserAlreadyExistsException();
        }

        // 1. Kreiraj user i save
        User user = requestMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setStatus(UserStatus.INITIALIZED);
        userRepository.save(user);  // user.getId() sada ima UUID

        // 2. Kreiraj token
        String token = UUID.randomUUID().toString();
        ActivationToken activationToken = new ActivationToken();
        activationToken.setUser(user);   // MapsId uzima userId iz user.getId()
        activationToken.setToken(token);

        tokenRepository.save(activationToken);

        // 3. Pošalji u RabbitMQ
        notificationProducer.sendActivationEmail(user.getEmail(),user.getUsername(), token);

        return token;
    }


    public void activateUser(String token) {
        ActivationToken activationToken = tokenRepository.findByToken(token)
                .orElseThrow(TokenIsInvalid::new);

        // TTL = 24 hours (adjust if needed)
        LocalDateTime created = activationToken.getCreatedAt();
        LocalDateTime now = LocalDateTime.now();
        if (created.isBefore(now.minusHours(24))) {
            // cleanup expired token and fail
            tokenRepository.delete(activationToken);
            throw new TokenIsInvalid();
        }

        User user = activationToken.getUser();
        user.setStatus(UserStatus.ACTIVE);


        userRepository.save(user);

        // Delete token after successful activation
        tokenRepository.delete(activationToken);
    }


    public String login(LoginRequest request) {
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(InvalidCredentialsException::new);



        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        if(user.getStatus() == UserStatus.INITIALIZED) {
            throw new UserNotActivatedException();
        }
        if (user.getStatus() == UserStatus.BLOCKED) {
            throw new UserBlockedException();
        }

        return jwtUtil.generateToken(user);
    }
    @Transactional
    public void forgotPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(InvalidCredentialsException::new);

        String token = UUID.randomUUID().toString();

        PasswordResetToken resetToken = passwordResetTokenRepository.findById(user.getId())
                .orElse(new PasswordResetToken());

        resetToken.setUser(user);
        resetToken.setToken(token);
        resetToken.setCreatedAt(LocalDateTime.now());

        passwordResetTokenRepository.save(resetToken);

        notificationProducer.sendPasswordResetEmail(email,user.getUsername(),token);
    }

    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(request.token()).orElseThrow(TokenIsInvalid::new);

        if (passwordResetToken.getCreatedAt().isBefore(LocalDateTime.now().minusMinutes(15))) {
            passwordResetTokenRepository.delete(passwordResetToken);
            throw new RuntimeException("Token expired!");
        }

        User user = passwordResetToken.getUser();
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);



        passwordResetTokenRepository.delete(passwordResetToken);
    }
}

