package com.ninjashop.ninjashop.service;

import com.ninjashop.ninjashop.exception.OrderException;
import com.ninjashop.ninjashop.model.Order;
import com.ninjashop.ninjashop.model.User;
import com.ninjashop.ninjashop.request.AddressRequest;
import jakarta.validation.Valid;

import java.util.List;

public interface OrderService {
    public Order orderTrack(String trackId) throws OrderException;
    public Order createOrder(User user, @Valid AddressRequest address) throws OrderException;
    public Order findOrderById(Long orderId) throws OrderException;
    public List<Order> userOrderHistoryLists(Long userId) throws OrderException;
    public Order placedOrder(Long orderId) throws OrderException;
    public Order orderComfired(Long orderId) throws OrderException;
    public Order shippedOrder(Long orderId) throws OrderException;
    public Order deliveredOrder(Long orderId) throws OrderException;
    public Order cancelOrder(Long orderId) throws OrderException;
    public List<Order> getAllOrder() throws OrderException;
    public void deleteOrder(Long orderId) throws OrderException;
}
