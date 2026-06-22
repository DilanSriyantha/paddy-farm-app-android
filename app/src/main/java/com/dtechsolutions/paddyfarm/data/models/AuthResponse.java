package com.dtechsolutions.paddyfarm.data.models;

public class AuthResponse {
    private String access_token;

    public void setAccessToken(String accessToken) {
        this.access_token = accessToken;
    }

    public String getAccessToken() { return access_token; }
}
