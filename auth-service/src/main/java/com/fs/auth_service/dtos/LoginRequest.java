package com.fs.auth_service.dtos;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}