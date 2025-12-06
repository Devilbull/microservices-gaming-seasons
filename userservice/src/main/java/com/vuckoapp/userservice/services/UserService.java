// java
    package com.vuckoapp.userservice.services;

    import com.vuckoapp.userservice.dto.CreateUserRequest;
    import com.vuckoapp.userservice.dto.UserDto;
    import com.vuckoapp.userservice.model.Role;
    import com.vuckoapp.userservice.model.User;
    import com.vuckoapp.userservice.model.UserStatus;
    import com.vuckoapp.userservice.repository.UserRepository;
    import lombok.RequiredArgsConstructor;
    import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Transactional;

    import java.util.List;
    import java.util.Objects;
    import java.util.UUID;

    @Service
    @RequiredArgsConstructor
    public class UserService {
        private final UserRepository repo;



        @Transactional(readOnly = true)
        public List<UserDto> all() {
            return repo.findAll().stream().map(this::toDto).toList();
        }

        @Transactional(readOnly = true)
        public UserDto byId(UUID id) {
            User u = repo.findById(id).orElseThrow();
            return toDto(u);
        }

        @Transactional
        public UserDto create(CreateUserRequest req) {
            User u = toEntity(req);
            return toDto(repo.save(u));
        }

        private User toEntity(CreateUserRequest r) {
            Objects.requireNonNull(r.username(), "username is required");
            Objects.requireNonNull(r.password(), "password is required");
            Objects.requireNonNull(r.fullName(), "fullName is required");
            Objects.requireNonNull(r.email(), "email is required");
            Objects.requireNonNull(r.dateOfBirth(), "dateOfBirth is required");
            Objects.requireNonNull(r.role(), "role is required");

            return User.builder()
                    .username(r.username())
                    .password(r.password()) // hashed by DB trigger/Flyway
                    .fullName(r.fullName())
                    .email(r.email())
                    .dateOfBirth(r.dateOfBirth())
                    .role(Role.valueOf(r.role()))
                    .status(UserStatus.valueOf("INITIALIZED"))
                    .isActivated(false)
                    .build();
        }

        private UserDto toDto(User u) {
            return new UserDto(
                    u.getId(),
                    u.getUsername(),
                    u.getFullName(),
                    u.getEmail(),
                    u.getDateOfBirth(),
                    "u.getRole()",
                    "u.getStatus()",
                    u.isActivated()
            );
        }
    }