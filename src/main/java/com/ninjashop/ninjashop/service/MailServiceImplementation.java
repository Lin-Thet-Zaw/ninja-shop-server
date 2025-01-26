package com.ninjashop.ninjashop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class MailServiceImplementation implements MailService {

    private static final Logger logger = LoggerFactory.getLogger(MailServiceImplementation.class);

    @Value("${app.frontend.url}")
    private String baseUrl;

    @Autowired
    JavaMailSender mailSender;

    @Override
    public String SendPasswordResetMail(String sendTo, String token) {
        try {
            String resetUrl = baseUrl + "/reset-password?token=" + token;
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(sendTo);
            message.setSubject("Password reset request");
            message.setText("To reset your password, click the link below:\n" + resetUrl);

            logger.info("Sending email to: {}", sendTo);
            logger.info("Reset URL: {}", resetUrl);

            mailSender.send(message);
            return "Password reset link sent to email!";
        } catch (MailException e) {
            logger.error("Failed to send email: {}", e.getMessage(), e);
            return "Failed to send email: " + e.getMessage();
        }
    }
}