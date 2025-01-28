package com.ninjashop.ninjashop.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class AddressRequest {
    @NotBlank(message = "First name is required")
    @Size(min = 1, max = 50, message = "First name must be between 1 and 50 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 1, max = 50, message = "Last name must be between 1 and 50 characters")
    private String lastName;

    @NotBlank(message = "Address is required")
    @Size(min = 5, max = 100, message = "Address must be between 5 and 100 characters")
    private String streetAddress;

    @NotBlank(message = "City name is required")
    @Size(min = 2, max = 50, message = "City must be between 5 and 50 characters")
    private String city;


    @NotBlank(message = "State is required")
    @Size(min = 5, max = 100, message = "State must be between 5 and 100 characters")
    private String state;

    @Pattern(regexp = "^[0-9]{5}(?:-[0-9]{4})?$", message = "Invalid ZIP code format")
    private String zipCode;

    @NotBlank(message = "Phone number is required")
    @Size(min = 1, max = 15, message = "Phone number must be between 5 and 50 characters")
    private String mobile;

    public @NotBlank(message = "Phone number is required") @Size(min = 1, max = 15, message = "Phone number must be between 5 and 50 characters") String getMobile() {
        return mobile;
    }

    public void setMobile(@NotBlank(message = "Phone number is required") @Size(min = 1, max = 15, message = "Phone number must be between 5 and 50 characters") String mobile) {
        this.mobile = mobile;
    }

    public @NotBlank(message = "First name is required") @Size(min = 1, max = 50, message = "First name must be between 1 and 50 characters") String getFirstName() {
        return firstName;
    }

    public void setFirstName(@NotBlank(message = "First name is required") @Size(min = 1, max = 50, message = "First name must be between 1 and 50 characters") String firstName) {
        this.firstName = firstName;
    }

    public @NotBlank(message = "Last name is required") @Size(min = 1, max = 50, message = "Last name must be between 1 and 50 characters") String getLastName() {
        return lastName;
    }

    public void setLastName(@NotBlank(message = "Last name is required") @Size(min = 1, max = 50, message = "Last name must be between 1 and 50 characters") String lastName) {
        this.lastName = lastName;
    }

    public @NotBlank(message = "Address is required") @Size(min = 5, max = 100, message = "Address must be between 5 and 100 characters") String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(@NotBlank(message = "Address is required") @Size(min = 5, max = 100, message = "Address must be between 5 and 100 characters") String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public @NotBlank(message = "City name is required") @Size(min = 2, max = 50, message = "City must be between 5 and 50 characters") String getCity() {
        return city;
    }

    public void setCity(@NotBlank(message = "City name is required") @Size(min = 2, max = 50, message = "City must be between 5 and 50 characters") String city) {
        this.city = city;
    }

    public @NotBlank(message = "State is required") @Size(min = 5, max = 100, message = "State must be between 5 and 100 characters") String getState() {
        return state;
    }

    public void setState(@NotBlank(message = "State is required") @Size(min = 5, max = 100, message = "State must be between 5 and 100 characters") String state) {
        this.state = state;
    }

    public @Pattern(regexp = "^[0-9]{5}(?:-[0-9]{4})?$", message = "Invalid ZIP code format") String getZipCode() {
        return zipCode;
    }

    public void setZipCode(@Pattern(regexp = "^[0-9]{5}(?:-[0-9]{4})?$", message = "Invalid ZIP code format") String zipCode) {
        this.zipCode = zipCode;
    }
}
