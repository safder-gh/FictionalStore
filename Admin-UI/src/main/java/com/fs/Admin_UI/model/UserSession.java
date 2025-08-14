package com.fs.Admin_UI.model;

public class UserSession {
    private String username;
    private String jwtToken;

    public UserSession(String username, String jwtToken) {
        this.username = username;
        this.jwtToken = jwtToken;
    }

    public String getUsername() { return username; }
    public String getJwtToken() { return jwtToken; }
}
