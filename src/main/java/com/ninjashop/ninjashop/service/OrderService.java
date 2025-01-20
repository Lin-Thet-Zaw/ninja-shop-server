package com.ninjashop.ninjashop.service;

import com.ninjashop.ninjashop.exception.OrderException;
import com.ninjashop.ninjashop.model.Address;
import com.ninjashop.ninjashop.model.Order;
import com.ninjashop.ninjashop.model.User;
import org.aspectj.weaver.ast.Or;

import java.util.List;

public interface OrderService {
    public Order createOrder(User user, Address address);
    public Order findOrderById(Long orderId) throws OrderException;
    public List<Order> userOrderHistoryLists(Long userId);
    public Order placedOrder(Long orderId) throws OrderException;
    public Order orderComfired(Long orderId) throws OrderException;
    public Order shippedOrder(Long orderId) throws OrderException;
    public Order deliveredOrder(Long orderId) throws OrderException;
    public Order cancelOrder(Long orderId) throws OrderException;
    public List<Order> getAllOrder();
    public void deleteOrder(Long orderId) throws OrderException;
}
