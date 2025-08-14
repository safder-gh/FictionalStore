package com.fs.auth_service.services;

import com.fs.auth_service.dao.RoleRepository;
import com.fs.auth_service.dao.UserRepository;
import com.fs.auth_service.model.Role;
import com.fs.auth_service.model.User;
import com.fs.auth_service.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public User register(String username, String rawPassword) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists");
        }
        var defaultRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new IllegalStateException("ROLE_USER not found, seed DB first"));

        User u = User.builder()
                .username(username)
                .passwordHash(passwordEncoder.encode(rawPassword))
                .build();
        u.getRoles().add(defaultRole);
        return userRepository.save(u);
    }

    public String login(String username, String rawPassword) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        if (!passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        List<String> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toList());
        return jwtUtil.generateToken(user.getUsername(), roles);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
