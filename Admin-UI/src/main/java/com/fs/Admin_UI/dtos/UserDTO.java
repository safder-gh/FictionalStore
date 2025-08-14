package com.fs.Admin_UI.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDTO {
    private String username;

    public UserDTO() {}

    public UserDTO(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
