package com.ninjashop.ninjashop.service;

import com.ninjashop.ninjashop.exception.ProductException;
import com.ninjashop.ninjashop.model.Review;
import com.ninjashop.ninjashop.model.User;
import com.ninjashop.ninjashop.request.ReviewRequest;

import java.util.List;

public interface ReviewService {
    public Review createReview(ReviewRequest req, User user) throws ProductException;
    public List<Review> getAllReview(Long productId);
}
