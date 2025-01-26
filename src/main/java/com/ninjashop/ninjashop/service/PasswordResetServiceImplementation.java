package com.ninjashop.ninjashop.service;
import com.ninjashop.ninjashop.exception.UserException;
import com.ninjashop.ninjashop.model.PasswordResetToken;
import com.ninjashop.ninjashop.model.User;
import com.ninjashop.ninjashop.repository.PasswordResetRepository;
import com.ninjashop.ninjashop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetServiceImplementation implements PasswordResetService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetRepository passwordResetRepository;

    @Override
    public String generateResetToken(String email) throws UserException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserException("User with email " + email + " not found");
        }

        // Check if a token already exists for the user
        Optional<PasswordResetToken> existingToken = passwordResetRepository.findByUser(user);

        PasswordResetToken passwordResetToken;
        if (existingToken.isPresent()) {
            // Update the existing token
            passwordResetToken = existingToken.get();
            passwordResetToken.setToken(generateToken()); // Generate a new token
            passwordResetToken.setExpiryDate(calculateExpiryDate()); // Set a new expiry date
        } else {
            // Create a new token
            passwordResetToken = new PasswordResetToken();
            passwordResetToken.setUser(user);
            passwordResetToken.setToken(generateToken());
            passwordResetToken.setExpiryDate(calculateExpiryDate());
        }

        // Save the token
        passwordResetRepository.save(passwordResetToken);

        return passwordResetToken.getToken();
    }

    private String generateToken() {
        // Generate a unique token (e.g., using UUID)
        return UUID.randomUUID().toString();
    }

    private LocalDateTime calculateExpiryDate() {
        // Set expiry date to 1 hour from now
        return LocalDateTime.now().plusMinutes(5);
    }
}