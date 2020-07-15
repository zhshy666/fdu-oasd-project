package com.oasd.backend.controller.request;

public class LoginRequest {
    private String usernameOrEmail;
    private String password;

    public LoginRequest() {
        // no codes here
    }


    public String getUsernameOrEmail() {
        return usernameOrEmail;
    }

    public void setUsername(String username) {
        this.usernameOrEmail = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
