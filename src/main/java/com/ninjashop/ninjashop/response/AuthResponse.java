package com.ninjashop.ninjashop.response;

public class AuthResponse {
    private String jwt;
    private String message;

    public AuthResponse() {
    }

    public AuthResponse(String jwt, String message) {
        this.jwt = jwt;
        this.message = message;
    }

    // Constructor that accepts only jwt
    public AuthResponse(String jwt) {
        this.jwt = jwt;
        this.message = "Success";  // You can set a default message
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

