package com.example.backendshop.controller;

import com.example.backendshop.model.Order;
import com.example.backendshop.respone.ResponseMessage;
import com.example.backendshop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin("*")
public class OrderController {

    @Autowired
    private OrderService orderService;

    //  1. Tạo đơn hàng
    @PostMapping("/create")
    public ResponseEntity<ResponseMessage> createOrder(@RequestBody Order order) {
        try {
            Order saved = orderService.createOrder(order);
            return ResponseEntity.ok(new ResponseMessage("Tạo đơn hàng thành công!", saved));
        } catch (Exception e) {
            // Ghi log nếu cần: e.printStackTrace();
            return ResponseEntity.status(500).body(new ResponseMessage("Tạo đơn hàng thất bại!", null));
        }
    }

    //  2. Admin xem tất cả đơn hàng
    @GetMapping("/all")
    public ResponseEntity<ResponseMessage> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok(new ResponseMessage("Lấy danh sách tất cả đơn hàng", orders));
    }

    //  3. User xem đơn hàng của chính mình
    @GetMapping
    public ResponseEntity<ResponseMessage> getOrdersByEmail(@RequestParam String email) {
        List<Order> orders = orderService.getOrdersByEmail(email);
        if (orders.isEmpty()) {
            return ResponseEntity.status(404).body(new ResponseMessage("Không tìm thấy đơn hàng", null));
        }
        return ResponseEntity.ok(new ResponseMessage("Lấy đơn hàng thành công", orders));
    }

    //  4. Admin cập nhật trạng thái đơn hàng
    @PatchMapping("/status")
    public ResponseEntity<ResponseMessage> updateStatus(
            @RequestParam String id,
            @RequestParam String status) {

        boolean updated = orderService.updateOrderStatus(id, status);
        if (updated) {
            return ResponseEntity.ok(new ResponseMessage("Cập nhật trạng thái thành công", null));
        } else {
            return ResponseEntity.status(404).body(new ResponseMessage("Không tìm thấy đơn hàng", null));
        }
    }
    @DeleteMapping("/delete")
    public ResponseEntity<ResponseMessage> deleteOrder(@RequestParam String id) {
        boolean deleted = orderService.deleteOrderById(id);
        if (deleted) {
            return ResponseEntity.ok(new ResponseMessage("Xoá đơn hàng thành công!", null));
        } else {
            return ResponseEntity.status(404).body(new ResponseMessage("Không tìm thấy đơn hàng để xoá", null));
        }
    }
}
