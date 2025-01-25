package com.ninjashop.ninjashop.service;

import com.ninjashop.ninjashop.exception.OrderException;
import com.ninjashop.ninjashop.model.*;
import com.ninjashop.ninjashop.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class orderServiceImplementation implements OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CartService cartService;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private OrderItemRepository orderItemRepository;

    public orderServiceImplementation(OrderRepository orderRepository, CartService cartService, AddressRepository addressRepository, UserRepository userRepository, OrderItemService orderItemService, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.cartService = cartService;
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
        this.orderItemService = orderItemService;
        this.orderItemRepository = orderItemRepository;
    }

    private String generateOrderTrackingCode() {
        Random random = new Random();
        String code;
        do {
            code = String.valueOf(random.nextInt(90000000) + 10000000); // Generates a random 8-digit number
        } while (orderRepository.findByOrderId(code).isPresent()); // Check if the code already exists
        return code;
    }

    @Override
    public Order createOrder(User user, Address shippingAddress) {
        shippingAddress.setUser(user);
        Address address = addressRepository.save(shippingAddress);
        user.getAddresses().add(address);
        userRepository.save(user);
        Cart cart = cartService.findUserCart(user.getId());

        List<OrderItem> orderItems = new ArrayList<>();
        for(CartItem item: cart.getCartItems()){
            OrderItem orderItem = new OrderItem();
            orderItem.setPrice(item.getPrice());
            orderItem.setProduct(item.getProduct());
            orderItem.setQuentity(item.getQuantity());
            orderItem.setSize(item.getSize());
            orderItem.setUserId(item.getUserId());
            orderItem.setDescountedPrice(item.getDiscountedPrice());

            OrderItem createOrderItem = orderItemRepository.save(orderItem);
            orderItems.add(createOrderItem);
        }

        Order createdOrder = new Order();
        createdOrder.setUser(user);
        createdOrder.setOrderItemList(orderItems);
        createdOrder.setTotalPrice(cart.getTotalPrice());
        createdOrder.setDiscounted(cart.getDiscounted());
        createdOrder.setTotalDiscountPrice(cart.getTotalDiscountedPrice());
        createdOrder.setTotalItem(cart.getTotalItem());
        createdOrder.setShippingAddress(address);
        createdOrder.setOrderDate(LocalDateTime.now());
        createdOrder.setOrderStatus("PENDING");
        createdOrder.getPaymentDetails().setStatus("PENDING");
        createdOrder.setCreatedAt(LocalDateTime.now());

        // Generate and set the order tracking code
        createdOrder.setOrderId(generateOrderTrackingCode());

        Order savedOrder = orderRepository.save(createdOrder);

        for(OrderItem item: orderItems){
            item.setOrder(savedOrder);
            orderItemRepository.save(item);
        }
        return savedOrder;
    }

    @Override
    public Order findOrderById(Long orderId) throws OrderException {
        Optional<Order> opt = orderRepository.findById(orderId);
        if (opt.isPresent()){
            return  opt.get();
        }
        throw new OrderException("Order Not Found");
    }

    @Override
    public List<Order> userOrderHistoryLists(Long userId) {
        List<Order> orders = orderRepository.getUsersOrders(userId);
        return  orders;
    }

    @Override
    public Order placedOrder(Long orderId) throws OrderException {
        Order order = findOrderById(orderId);
        order.setOrderStatus("PLACED");
        order.getPaymentDetails().setStatus("COMPLETED");
        return orderRepository.save(order);
    }

    @Override
    public Order orderComfired(Long orderId) throws OrderException {
        Order order = findOrderById(orderId);
        order.setOrderStatus("CONFIRMED");

        return orderRepository.save(order);
    }

    @Override
    public Order shippedOrder(Long orderId) throws OrderException {
        Order order = findOrderById(orderId);
        order.setOrderStatus("SHIPPED");
        return orderRepository.save(order);
    }

    @Override
    public Order deliveredOrder(Long orderId) throws OrderException {
        Order order = findOrderById(orderId);
        order.setOrderStatus("DELIVERED");
        return orderRepository.save(order);
    }

    @Override
    public Order cancelOrder(Long orderId) throws OrderException {
        Order order = findOrderById(orderId);
        order.setOrderStatus("CANCELLED");
        return orderRepository.save(order);
    }

    @Override
    public List<Order> getAllOrder() {
        return orderRepository.findAll();
    }

    @Override
    public void deleteOrder(Long orderId) throws OrderException {
        Order order = findOrderById(orderId);

        orderRepository.delete(order);
    }
}
