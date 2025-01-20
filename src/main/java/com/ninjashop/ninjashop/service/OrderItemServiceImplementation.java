package com.ninjashop.ninjashop.service;

import com.ninjashop.ninjashop.model.OrderItem;
import com.ninjashop.ninjashop.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderItemServiceImplementation implements OrderItemService{

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Override
    public OrderItem createOrderItem(OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }
}
