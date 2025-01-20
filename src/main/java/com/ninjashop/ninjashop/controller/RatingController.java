package com.ninjashop.ninjashop.controller;

import com.ninjashop.ninjashop.exception.ProductException;
import com.ninjashop.ninjashop.exception.UserException;
import com.ninjashop.ninjashop.model.Rating;
import com.ninjashop.ninjashop.model.User;
import com.ninjashop.ninjashop.request.RatingRequest;
import com.ninjashop.ninjashop.service.RatingService;
import com.ninjashop.ninjashop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {
    @Autowired
    private UserService userService;

    @Autowired
    private RatingService ratingService;

    @PostMapping("/create")
    public ResponseEntity<Rating> createRating(@RequestBody RatingRequest req,
                                               @RequestHeader("Authorization") String jwt) throws UserException, ProductException {
        User user = userService.findUserProfileByJwt(jwt);
        Rating rating = ratingService.createRating(req,user);

        return new ResponseEntity<>(rating, HttpStatus.CREATED);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Rating>> getProductRating(@PathVariable Long productId,
                                                         @RequestHeader("Authorization") String jwt) throws UserException, ProductException{
        User user = userService.findUserProfileByJwt(jwt);
        List<Rating> ratings = ratingService.getProductRatings(productId);

        return new ResponseEntity<>(ratings,HttpStatus.CREATED);
    }
}
