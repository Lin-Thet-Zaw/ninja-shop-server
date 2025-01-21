package com.ninjashop.ninjashop.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class RegisterRequest {
    @NotNull(message = "Email is required") // Ensures email is not null
    @Email(message = "Invalid email format") // Ensures email format is valid
    private String email;

    @NotNull(message = "Password is required") // Ensures password is not null
    @Size(min = 8, message = "Password must be at least 8 characters long") // Ensures password has at least 6 characters
    private String password;

    @NotBlank(message = "First Name is required")
    private String firstName;

    @NotBlank(message = "Last Name Required")
    private String lastName;

    public @NotBlank(message = "First Name is required") String getFirstName() {
        return firstName;
    }

    public void setFirstName(@NotBlank(message = "First Name is required") String firstName) {
        this.firstName = firstName;
    }

    public @NotBlank(message = "Last Name Required") String getLastName() {
        return lastName;
    }

    public void setLastName(@NotBlank(message = "Last Name Required") String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
