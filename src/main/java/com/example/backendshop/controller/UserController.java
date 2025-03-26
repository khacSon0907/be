package com.example.backendshop.controller;

import com.example.backendshop.Login.LoginRequest;
import com.example.backendshop.model.Cart;
import com.example.backendshop.model.CartItem;
import com.example.backendshop.model.User;
import com.example.backendshop.respone.ResponseMessage;
import com.example.backendshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/")
public class UserController {

    @Autowired
    private UserService userService;

    @DeleteMapping("/cart/remove")
    public ResponseEntity<?> removeFromCart(@RequestParam String email,
                                            @RequestParam String productId,
                                            @RequestParam String size) {
        boolean removed = userService.removeItemFromUserCart(email, productId, size);
        if (removed) {
            return ResponseEntity.ok(new ResponseMessage("Đã xóa sản phẩm khỏi giỏ hàng", null));
        } else {
            return ResponseEntity.status(404)
                    .body(new ResponseMessage("Không tìm thấy user hoặc sản phẩm", null));
        }
    }

    @PostMapping("/cart/add")
    public ResponseEntity<?> addToCart(@RequestParam String email, @RequestBody CartItem newItem) {
        User updatedUser = userService.addItemToUserCart(email, newItem);
        if (updatedUser != null) {
            return ResponseEntity.ok().body(new ResponseMessage("Đã thêm vào giỏ hàng", updatedUser.getCart()));
        } else {
            return ResponseEntity.status(404).body(new ResponseMessage("Không tìm thấy user", null));
        }
    }

    @GetMapping("/cart")
    public ResponseEntity<?> getCartByEmail(@RequestParam String email) {
        Cart cart = userService.getUserCart(email);
        if (cart != null) {
            return ResponseEntity.ok(cart);
        } else {
            return ResponseEntity.status(404).body(new ResponseMessage("Không tìm thấy user hoặc giỏ rỗng", null));
        }
    }

    // Endpoint để lấy thông tin người dùng theo email
    @GetMapping("/user/email/{email}")
    public Map<String, Object> getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email);
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
    @PatchMapping("/user/update")
    public ResponseEntity<?> updateUserByEmail(@RequestParam String email, @RequestBody Map<String, Object> updates) {
        User user = userService.updateUserByEmail(email, updates);

        if (user != null) {
            return ResponseEntity.ok().body(
                    new ResponseMessage("Cập nhật thành công", user)
            );
        } else {
            return ResponseEntity.status(404).body(
                    new ResponseMessage("Thất bại, không tìm thấy email", null)
            );
        }
    }
}
