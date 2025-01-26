package com.ninjashop.ninjashop.repository;

import com.ninjashop.ninjashop.model.PasswordResetToken;
import com.ninjashop.ninjashop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);

    Optional<PasswordResetToken> findByUser(User user);
}
