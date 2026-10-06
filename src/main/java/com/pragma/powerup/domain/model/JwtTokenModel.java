package com.pragma.powerup.domain.model;

public class JwtTokenModel {
    private String token;

    public JwtTokenModel() {
    }

    public JwtTokenModel(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
