package com.eicon.demo.repositories;

import com.eicon.demo.entities.Order;
import com.eicon.demo.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByOrder(Order order);
}
