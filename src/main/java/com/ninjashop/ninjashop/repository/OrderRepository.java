package com.ninjashop.ninjashop.repository;

import com.ninjashop.ninjashop.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o WHERE o.user.id = :userId")
    List<Order> getUsersOrders(@Param("userId") Long userId);
    Optional<Order> findByOrderId(String orderId);
}
