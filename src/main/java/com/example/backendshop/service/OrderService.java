package com.example.backendshop.service;

import com.example.backendshop.model.Order;
import com.example.backendshop.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    // Lấy tất cả đơn hàng
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // Lấy đơn hàng theo email
    public List<Order> getOrdersByEmail(String email) {
        return orderRepository.findByEmail(email);
    }

    //  Lấy đơn theo ID
    public Order getOrderById(String id) {
        return orderRepository.findById(id).orElse(null);
    }

    //  Tạo đơn hàng mới
    public Order createOrder(Order order) {
        order.setId(null); // để Mongo tự sinh _id
        order.setStatus("pending");
        order.setCreatedAt(new Date());
        return orderRepository.save(order);
    }

    //  Cập nhật trạng thái đơn hàng
    public boolean updateOrderStatus(String orderId, String newStatus) {
        Optional<Order> optional = orderRepository.findById(orderId);
        if (optional.isPresent()) {
            Order order = optional.get();
            order.setStatus(newStatus);
            orderRepository.save(order); // lưu lại sau khi đổi trạng thái
            return true;
        }
        return false;
    }
    public boolean deleteOrderById(String id) {
        if (orderRepository.existsById(id)) {
            orderRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
