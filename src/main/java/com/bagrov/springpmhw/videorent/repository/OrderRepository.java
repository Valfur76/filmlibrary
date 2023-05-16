package com.bagrov.springpmhw.videorent.repository;

import com.bagrov.springpmhw.videorent.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByUserId(int userId);
}
