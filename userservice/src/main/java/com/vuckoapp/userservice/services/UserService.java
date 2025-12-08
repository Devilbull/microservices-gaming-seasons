// java
package com.vuckoapp.userservice.services;

import com.vuckoapp.userservice.dto.CreateUserRequest;
import com.vuckoapp.userservice.dto.UserDto;
import com.vuckoapp.userservice.model.User;
import com.vuckoapp.userservice.repository.UserRepository;
import com.vuckoapp.userservice.services.mapper.CreateUserRequestMapper;
import com.vuckoapp.userservice.services.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repo;
    private final UserMapper userMapper;
    private final CreateUserRequestMapper requestMapper;

    @Transactional(readOnly = true)
    public List<UserDto> all() {
        return repo.findAll().stream()
                .map(userMapper::toDto)
                .toList();
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
}
