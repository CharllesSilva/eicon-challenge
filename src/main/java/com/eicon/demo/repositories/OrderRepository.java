package com.eicon.demo.repositories;

import com.eicon.demo.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o WHERE o.registrationDate >= :startOfDay AND o.registrationDate <= :endOfDay")
    List<Order> findByRegistrationDateBetween(@Param("startOfDay") Instant startOfDay, @Param("endOfDay") Instant endOfDay);
}

