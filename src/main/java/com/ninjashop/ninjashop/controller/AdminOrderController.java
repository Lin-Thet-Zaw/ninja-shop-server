package com.ninjashop.ninjashop.controller;

import com.ninjashop.ninjashop.exception.OrderException;
import com.ninjashop.ninjashop.exception.UserException;
import com.ninjashop.ninjashop.model.Order;
import com.ninjashop.ninjashop.model.User;
import com.ninjashop.ninjashop.response.ApiResponse;
import com.ninjashop.ninjashop.service.OrderService;
import com.ninjashop.ninjashop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/orders")
public class AdminOrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;

    @GetMapping("/")
    public ResponseEntity<List<Order>> getAllOrderHandler() throws OrderException {
        List<Order> orders = orderService.getAllOrder();
        return new ResponseEntity<>(orders, HttpStatus.ACCEPTED);
    }

    @PutMapping("/{orderId}/confirmed")
    public ResponseEntity<?> confirmedOrderHandler(@PathVariable Long orderId,
                                                       @RequestHeader("Authorization") String jwt) throws OrderException, UserException {
        // Authenticate user and check role
        User user = userService.findUserProfileByJwt(jwt);
        if (!"admin".equals(user.getRole())) {
            ApiResponse response = new ApiResponse("Unauthorized access", false);
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
        Order order = orderService.orderComfired(orderId);
        return  new ResponseEntity<>(order,HttpStatus.OK);
    }

    @PutMapping("/{orderId}/ship")
    public ResponseEntity<?> shippedOrderHandler(@PathVariable Long orderId,
                                                    @RequestHeader("Authorization") String jwt) throws OrderException, UserException {
        // Authenticate user and check role
        User user = userService.findUserProfileByJwt(jwt);
        if (!"admin".equals(user.getRole())) {
            ApiResponse response = new ApiResponse("Unauthorized access", false);
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
        Order order = orderService.shippedOrder(orderId);
        return  new ResponseEntity<>(order,HttpStatus.OK);
    }

    @PutMapping("/{orderId}/deliver")
    public ResponseEntity<?> deliveredOrderHandler(@PathVariable Long orderId,
                                                      @RequestHeader("Authorization") String jwt) throws OrderException, UserException {
        // Authenticate user and check role
        User user = userService.findUserProfileByJwt(jwt);
        if (!"admin".equals(user.getRole())) {
            ApiResponse response = new ApiResponse("Unauthorized access", false);
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
        Order order = orderService.deliveredOrder(orderId);
        return  new ResponseEntity<>(order,HttpStatus.OK);
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<?> cancelOrderHandler(@PathVariable Long orderId,
                                                      @RequestHeader("Authorization") String jwt) throws OrderException, UserException {
        // Authenticate user and check role
        User user = userService.findUserProfileByJwt(jwt);
        if (!"admin".equals(user.getRole())) {
            ApiResponse response = new ApiResponse("Unauthorized access", false);
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
        Order order = orderService.cancelOrder(orderId);
        return  new ResponseEntity<>(order,HttpStatus.OK);
    }

    @DeleteMapping("/{orderId}/delete")
    public ResponseEntity<ApiResponse> deleteOrderHandler(@PathVariable Long orderId,
                                                      @RequestHeader("Authorization") String jwt) throws OrderException, UserException {
        // Authenticate user and check role
        User user = userService.findUserProfileByJwt(jwt);
        if (!"admin".equals(user.getRole())) {
            ApiResponse response = new ApiResponse("Unauthorized access", false);
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
        orderService.deleteOrder(orderId);
        ApiResponse res = new ApiResponse("Delete Order Successfully", true);
        return  new ResponseEntity<>(res,HttpStatus.OK);
    }

}
