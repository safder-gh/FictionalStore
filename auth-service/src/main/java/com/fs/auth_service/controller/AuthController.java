package com.fs.auth_service.controller;


import com.fs.auth_service.dtos.LoginRequest;
import com.fs.auth_service.dtos.RegisterRequest;
import com.fs.auth_service.model.User;
import com.fs.auth_service.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    @Autowired
    AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody RegisterRequest req) {
        var user = authService.register(req.getUsername(), req.getPassword());
        user.setPasswordHash(null); // hide hash in response
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        String token = authService.login(req.getUsername(), req.getPassword());
        return ResponseEntity.ok().body(java.util.Map.of("token", token));
    }
    @PostMapping("/api/auth/validate")
    public Map<String, Object> validateToken(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        // validate token...
        return Map.of(
                "username", "admin",
                "roles", List.of("ROLE_ADMIN", "ROLE_USER")
        );
    }

}
