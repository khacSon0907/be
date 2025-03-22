package com.example.backendshop.service;

import com.example.backendshop.model.Cart;
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

    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    public User updateUserByEmail(String email, Map<String, Object> updates) {
        Optional<User> existingUser = userRepository.findByEmail(email);

        if (existingUser.isPresent()) {
            User user = existingUser.get();

            // Kiểm tra từng trường có trong Map và cập nhật nếu có
            if (updates.containsKey("username")) {
                user.setUsername((String) updates.get("username"));
            }
            if (updates.containsKey("password")) {
                user.setPassword((String) updates.get("password"));
            }
            if (updates.containsKey("phonenumber")) {
                user.setPhonenumber((String) updates.get("phonenumber"));
            }
            if (updates.containsKey("userRole")) {
                user.setUserRole(UserRole.valueOf((String) updates.get("userRole")));
            }
            if (updates.containsKey("cart")) {
                user.setCart((Cart) updates.get("cart"));
            }

            return userRepository.save(user);
        }
        return null; // Không tìm thấy user
    }
}
