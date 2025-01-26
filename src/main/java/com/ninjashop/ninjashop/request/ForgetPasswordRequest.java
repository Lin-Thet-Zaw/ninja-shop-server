package com.ninjashop.ninjashop.request;

import jakarta.validation.constraints.Email;

public class ForgetPasswordRequest {
    @Email(message = "Invalid email format")
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}