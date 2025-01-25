package com.ninjashop.ninjashop.controller;

import com.ninjashop.ninjashop.exception.OrderException;
import com.ninjashop.ninjashop.model.Order;
import com.ninjashop.ninjashop.response.ApiResponse;
import com.ninjashop.ninjashop.service.OrderService;
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

    @GetMapping("/")
    public ResponseEntity<List<Order>> getAllOrderHandler() throws OrderException {
        List<Order> orders = orderService.getAllOrder();
        return new ResponseEntity<>(orders, HttpStatus.ACCEPTED);
    }

    @PutMapping("/{orderId}/comfirmed")
    public ResponseEntity<Order> comfirmedOrderHandler(@PathVariable Long orderId,
                                                       @RequestHeader("Authorization") String jwt) throws OrderException{
        Order order = orderService.orderComfired(orderId);
        return  new ResponseEntity<>(order,HttpStatus.OK);
    }

    @PutMapping("/{orderId}/ship")
    public ResponseEntity<Order> shipedOrderHanlder(@PathVariable Long orderId,
                                                    @RequestHeader("Authorization") String jwt) throws OrderException{
        Order order = orderService.shippedOrder(orderId);
        return  new ResponseEntity<>(order,HttpStatus.OK);
    }

    @PutMapping("/{orderId}/deliver")
    public ResponseEntity<Order> deliverdOrderHanlder(@PathVariable Long orderId,
                                                      @RequestHeader("Authorization") String jwt) throws OrderException{
        Order order = orderService.deliveredOrder(orderId);
        return  new ResponseEntity<>(order,HttpStatus.OK);
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<Order> cancelOrderHandler(@PathVariable Long orderId,
                                                      @RequestHeader("Authorization") String jwt) throws OrderException{
        Order order = orderService.cancelOrder(orderId);
        return  new ResponseEntity<>(order,HttpStatus.OK);
    }

    @DeleteMapping("/{orderId}/delete")
    public ResponseEntity<ApiResponse> deleteOrderHandler(@PathVariable Long orderId,
                                                      @RequestHeader("Authorization") String jwt) throws OrderException{
        orderService.deleteOrder(orderId);
        ApiResponse res = new ApiResponse();
        res.setMessage("Delete Order Successfully");
        res.setStatus(true);
        return  new ResponseEntity<>(res,HttpStatus.OK);
    }

}
