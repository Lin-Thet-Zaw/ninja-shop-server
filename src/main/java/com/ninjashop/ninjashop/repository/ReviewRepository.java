package com.ninjashop.ninjashop.repository;

import com.ninjashop.ninjashop.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("SELECT r FROM Review r WHERE r.product.id = :productId")
    List<Review> getAllReviews(@Param("productId") Long productId);
}

