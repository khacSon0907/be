package com.example.backendshop.controller;

import com.example.backendshop.Login.LoginRequest;
import com.example.backendshop.model.CartItem;
import com.example.backendshop.model.User;
import com.example.backendshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/")
public class UserController {

    @Autowired
    private UserService userService;

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateUser(@PathVariable String id, @RequestBody User updatedUser) {
        User updated = userService.updateUser(id, updatedUser);
        Map<String, Object> response = new HashMap<>();
        if (updated != null) {
            response.put("status", "success");
            response.put("user", updated);
            return ResponseEntity.ok(response);
        }
        response.put("status", "error");
        response.put("message", "User not found!");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody User user) {
        String result = userService.register(user);
        Map<String, Object> response = new HashMap<>();
        response.put("message", result);

        // Kiểm tra nếu có lỗi, trả về mã trạng thái 400 (Bad Request)
        if (result.equals("Username is required!") || result.equals("Email is required!") || result.equals("Password is required!") || result.equals("Email is already registered!")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest loginRequest) {
        Map<String, Object> loginResponse = userService.login(loginRequest.getEmail(), loginRequest.getPassword());
        Map<String, Object> response = new HashMap<>();

        if (loginResponse.get("data") != null) {
            response.put("message", loginResponse.get("message"));
            response.put("data", loginResponse.get("data"));
            return ResponseEntity.ok(response);
        } else {
            response.put("message", loginResponse.get("message"));
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @GetMapping("/users")
    public ResponseEntity<Map<String, Object>> getAllUsers() {
        List<User> users = userService.getAllUser();
        Map<String, Object> response = new HashMap<>();
        response.put("users", users);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<Map<String, Object>> getUserById(@PathVariable String id) {
        Optional<User> user = userService.getUserById(id);
        Map<String, Object> response = new HashMap<>();
        if (user.isPresent()) {
            response.put("status", "success");
            response.put("user", user.get());
            return ResponseEntity.ok(response);
        }
        response.put("status", "error");
        response.put("message", "User not found!");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @PostMapping("/{userId}/cart")
    public ResponseEntity<Map<String, Object>> addToCart(@PathVariable String userId, @RequestBody CartItem item) {
        userService.addToCart(userId, item);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Item added to cart");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{userId}/cart/{productId}")
    public ResponseEntity<Map<String, Object>> removeFromCart(@PathVariable String userId, @PathVariable String productId) {
        userService.removeFromCart(userId, productId);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Item removed from cart");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}/cart/total")
    public ResponseEntity<Map<String, Object>> getTotalCartPrice(@PathVariable String userId) {
        double total = userService.getTotalCartPrice(userId);
        Map<String, Object> response = new HashMap<>();
        response.put("total_price", total);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable String id) {
        boolean isDeleted = userService.deleteUser(id);
        Map<String, Object> response = new HashMap<>();
        if (isDeleted) {
            response.put("status", "success");
            response.put("message", "User deleted successfully!");
            return ResponseEntity.ok(response);
        }
        response.put("status", "error");
        response.put("message", "User not found!");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
