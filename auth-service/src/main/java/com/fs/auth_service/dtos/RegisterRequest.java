package com.fs.auth_service.dtos;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
}