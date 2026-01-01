// java
package com.vuckoapp.userservice.services;

import com.vuckoapp.userservice.dto.*;
import com.vuckoapp.userservice.exceptions.UserNotFoundException;
import com.vuckoapp.userservice.model.GamerStats;
import com.vuckoapp.userservice.model.Role;
import com.vuckoapp.userservice.model.User;
import com.vuckoapp.userservice.model.UserStatus;
import com.vuckoapp.userservice.repository.GamerStatsRepository;
import com.vuckoapp.userservice.repository.UserRepository;
import com.vuckoapp.userservice.services.mapper.CreateUserRequestMapper;
import com.vuckoapp.userservice.services.mapper.UserMapper;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repo;
    private final UserMapper userMapper;
    private final CreateUserRequestMapper requestMapper;
    private final GamerStatsRepository gamerStatsRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Page<UserDto> all(String username, Pageable pageable) {

        Page<User> page;

        if (username != null && !username.isBlank()) {
            page = repo.findByUsernameContainingIgnoreCase(username, pageable);
        } else {
            page = repo.findAll(pageable);
        }

        return page.map(userMapper::toDto);
    }

    @Transactional(readOnly = true)
    public UserDto byId(UUID id) {
        return repo.findById(id)
                .map(userMapper::toDto)
                .orElseThrow();
    }

    @Transactional
    public UserDto create(CreateUserRequest req) {
        User u = requestMapper.toEntity(req);
        return userMapper.toDto(repo.save(u));
    }

    @Transactional
    public UserDto updateUser(String username, UpdateUserRequest dto) {
        User user = repo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (dto.fullName() != null)
            user.setFullName(dto.fullName());

        if (dto.dateOfBirth() != null)
            user.setDateOfBirth(dto.dateOfBirth());
        repo.save(user);

        GamerStats statsDto = null;
        if(user.getRole().equals(Role.GAMER)){
            statsDto = gamerStatsRepository.findById(user.getId()).orElse(null);
        }
        return userMapper.toDto(user,statsDto);

    }
    @Transactional
    public UserDto changePassword(String name, ChangePasswordRequest req) {
        User user = repo.findByUsername(name)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if(!passwordEncoder.matches(req.oldPassword(), user.getPassword()))
            throw new RuntimeException("Incorrect Old password");

        user.setPassword(passwordEncoder.encode(req.newPassword()));



        return userMapper.toDto(repo.save(user));
    }

    @Transactional
    public UserDto getByUsername(String username) {
        User user = repo.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);
        GamerStats statsDto = null;
        if(user.getRole().equals(Role.GAMER)){
            statsDto = gamerStatsRepository.findById(user.getId()).orElse(null);
        }
        return userMapper.toDto(user,statsDto);
    }

    public void blockUser(UUID id) {
        User user = repo.findById(id)
                .orElseThrow(UserNotFoundException::new);
        if(user.getRole() == Role.ADMIN){
            throw new RuntimeException("Cannot block admin user");
        }
        user.setStatus(UserStatus.BLOCKED);
        repo.save(user);

    }

    public void unblockUser(UUID id) {
        User user = repo.findById(id)
                .orElseThrow(UserNotFoundException::new);
        user.setStatus(UserStatus.ACTIVE);
        repo.save(user);
    }

    @Transactional
    public UserDto getById(UUID id) {
        User user = repo.findById(id)
                .orElseThrow(UserNotFoundException::new);
        GamerStats statsDto = null;
        if(user.getRole().equals(Role.GAMER)){
            statsDto = gamerStatsRepository.findById(user.getId()).orElse(null);
        }
        return userMapper.toDto(user,statsDto);
    }
}
