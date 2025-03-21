package com.example.backendshop.service;

import com.example.backendshop.model.CartItem;
import com.example.backendshop.model.User;
import com.example.backendshop.model.UserRole;
import com.example.backendshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public boolean deleteUser(String id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Map<String, Object> getUserByEmail(String email) {
        Map<String, Object> response = new HashMap<>();
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            response.put("message", "Get User done !");
            response.put("data", userOptional.get());
            return response;
        } else {
            response.put("message", "User not found!");
            response.put("data", null);
            return response;
        }
    }


    public String register(User user) {
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            return "Username is required!";
        }
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            return "Email is required!";
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            return "Password is required!";
        }

        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            return "Email is already registered!";
        }

        // Mã hóa mật khẩu trước khi lưu
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        user.setUserRole(UserRole.CLIENT);
        userRepository.save(user);
        return "Đăng ký thành công!";
    }



    public Map<String, Object> login(String email, String password) {
        Map<String, Object> response = new HashMap<>();
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                response.put("message", "Login successful!");
                response.put("data", user);
                return response;
            } else {
                response.put("message", "Invalid password!");
                response.put("data", null);
                return response;
            }
        } else {
            response.put("message", "User not found!");
            response.put("data", null);
            return response;
        }
    }
    public User updateUser(String id, User updatedUser) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            User user = existingUser.get();

            if (updatedUser.getUsername() != null) {
                user.setUsername(updatedUser.getUsername());
            }
            if (updatedUser.getEmail() != null) {
                user.setEmail(updatedUser.getEmail());
            }
            if (updatedUser.getPassword() != null) {
                // Mã hóa mật khẩu mới trước khi cập nhật
                user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            }
            if (updatedUser.getPhonenumber() != null) {
                user.setPhonenumber(updatedUser.getPhonenumber());
            }
            if (updatedUser.getUserRole() != null) {
                user.setUserRole(updatedUser.getUserRole());
            }

            return userRepository.save(user);
        }
        return null;
    }

    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    public void addToCart(String userId, CartItem item) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.getCart().addItem(item);
            userRepository.save(user);
        }
    }

    public void removeFromCart(String userId, String productId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.getCart().removeItem(productId);
            userRepository.save(user);
        }
    }

    public double getTotalCartPrice(String userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        return userOpt.map(user -> user.getCart().getTotalPrice()).orElse(0.0);
    }
}
