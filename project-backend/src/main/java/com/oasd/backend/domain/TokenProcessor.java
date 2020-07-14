package com.oasd.backend.domain;

public class TokenProcessor {
    private String token;
    private String username;

    public TokenProcessor(){
        // no codes here
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
