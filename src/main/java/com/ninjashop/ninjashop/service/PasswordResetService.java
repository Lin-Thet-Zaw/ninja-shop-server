package com.ninjashop.ninjashop.service;

import com.ninjashop.ninjashop.exception.UserException;

public interface PasswordResetService {
    public String generateResetToken(String email) throws UserException;
}
