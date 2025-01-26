package com.ninjashop.ninjashop.controller;

import com.ninjashop.ninjashop.exception.UserException;
import com.ninjashop.ninjashop.model.PasswordResetToken;
import com.ninjashop.ninjashop.model.User;
import com.ninjashop.ninjashop.repository.PasswordResetRepository;
import com.ninjashop.ninjashop.repository.UserRepository;
import com.ninjashop.ninjashop.request.ForgetPasswordRequest;
import com.ninjashop.ninjashop.request.ResetPasswordRequest;
import com.ninjashop.ninjashop.service.MailService;
import com.ninjashop.ninjashop.service.PasswordResetService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class ResetPasswordController {

    @Autowired
    private PasswordResetService passwordResetService;

    @Autowired
    private PasswordResetRepository passwordResetRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailService mailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/forget-password")
    private ResponseEntity<?> forgetPassword(@Valid @RequestBody ForgetPasswordRequest req, BindingResult bindingResult) throws UserException {
        // Check for validation errors
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        try {
            String token = passwordResetService.generateResetToken(req.getEmail());
            mailService.SendPasswordResetMail(req.getEmail(), token);
            return new ResponseEntity<>("Password reset link sent to your email address", HttpStatus.ACCEPTED);
        } catch (UserException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>("A password reset token already exists for this user. Please check your email.", HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to send password reset link to your email", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        try {
            // Find the token in the database
            Optional<PasswordResetToken> resetToken = passwordResetRepository.findByToken(request.getToken());
            System.out.println(request);
            if (resetToken.isPresent() && resetToken.get().getExpiryDate().isAfter(LocalDateTime.now())) {
                // Token is valid and not expired
                User user = resetToken.get().getUser();
                user.setPassword(passwordEncoder.encode(request.getPassword())); // Update the password
                userRepository.save(user);

                // Delete the token after use
                passwordResetRepository.delete(resetToken.get());

                return new ResponseEntity<>("Password reset successfully", HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity<>("Invalid or expired token", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while resetting the password", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}