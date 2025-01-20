package com.ninjashop.ninjashop.service;

import com.ninjashop.ninjashop.exception.ProductException;
import com.ninjashop.ninjashop.model.Product;
import com.ninjashop.ninjashop.model.Review;
import com.ninjashop.ninjashop.model.User;
import com.ninjashop.ninjashop.repository.ProductRepository;
import com.ninjashop.ninjashop.repository.ReviewRepository;
import com.ninjashop.ninjashop.request.ReviewRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewServiceImplementation implements ReviewService{
    @Autowired
    private ProductService productService;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private ProductRepository productRepository;

    public ReviewServiceImplementation(ProductService productService, ReviewRepository reviewRepository, ProductRepository productRepository) {
        this.productService = productService;
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
    }

    @Override
    public Review createReview(ReviewRequest req, User user) throws ProductException {
        Product product = productService.findProductById(req.getProductId());

        Review review = new Review();
        review.setUser(user);
        review.setProduct(product);
        review.setReview(req.getReview());
        review.setCreatedAt(LocalDateTime.now());
        return  reviewRepository.save(review);
    }

    @Override
    public List<Review> getAllReview(Long productId) {
        return reviewRepository.getAllReviews(productId);
    }
}
