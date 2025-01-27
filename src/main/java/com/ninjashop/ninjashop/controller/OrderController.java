package com.ninjashop.ninjashop.controller;

import com.ninjashop.ninjashop.exception.OrderException;
import com.ninjashop.ninjashop.exception.UserException;
import com.ninjashop.ninjashop.model.Address;
import com.ninjashop.ninjashop.model.Order;
import com.ninjashop.ninjashop.model.User;
import com.ninjashop.ninjashop.request.AddressRequest;
import com.ninjashop.ninjashop.response.AuthResponse;
import com.ninjashop.ninjashop.service.OrderService;
import com.ninjashop.ninjashop.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;

    @PostMapping("/")
    public ResponseEntity<?> createOrder(@Valid @RequestBody AddressRequest shippingAddress,
                                         @RequestHeader("Authorization") String jwt, BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                StringBuilder errorMessage = new StringBuilder("Error");
                bindingResult.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()).append(" "));
                return new ResponseEntity<>(new AuthResponse(null, errorMessage.toString()), HttpStatus.BAD_REQUEST);
            }
            User user = userService.findUserProfileByJwt(jwt);
            Order order = orderService.createOrder(user, shippingAddress);
            System.out.println(order);
            return new ResponseEntity<>(order, HttpStatus.CREATED);
        } catch (UserException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while creating the order.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/user")
    public ResponseEntity<?> userOrderHistoryLists(@RequestHeader("Authorization") String jwt) {
        try {
            User user = userService.findUserProfileByJwt(jwt);
            List<Order> orders = orderService.userOrderHistoryLists(user.getId());
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } catch (UserException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while fetching the order history.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findOrderById(@PathVariable("id") Long orderId,
                                           @RequestHeader("Authorization") String jwt) {
        try {
            User user = userService.findUserProfileByJwt(jwt);
            Order order = orderService.findOrderById(orderId);
            return new ResponseEntity<>(order, HttpStatus.OK);
        } catch (UserException | OrderException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while fetching the order.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}/track")
    public ResponseEntity<?> findByOrderId(@PathVariable("id") String trackId){
        try {
            Order order = orderService.orderTrack(trackId);
            return new ResponseEntity<>(order, HttpStatus.OK);
        } catch (OrderException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while fetching the order.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{orderId}/confirmed")
    public ResponseEntity<?> confirmedOrderHandler(@PathVariable Long orderId,
                                                   @RequestHeader("Authorization") String jwt) {
        try {
            Order order = orderService.placedOrder(orderId);
            return new ResponseEntity<>(order, HttpStatus.OK);
        } catch (OrderException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while confirming the order.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}