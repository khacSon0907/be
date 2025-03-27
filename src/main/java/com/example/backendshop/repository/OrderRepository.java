package com.example.backendshop.repository;

import com.example.backendshop.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {

    // ✅ Lấy tất cả đơn hàng của 1 user theo email
    List<Order> findByEmail(String email);
}
