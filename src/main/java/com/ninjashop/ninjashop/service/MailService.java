package com.ninjashop.ninjashop.service;

public interface MailService {
    public String SendPasswordResetMail(String sendTo, String token);
}
