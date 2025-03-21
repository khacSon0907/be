package com.example.backendshop.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "users") // Chỉ định MongoDB collection
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    private String id;
    private String username;
    private String password;
    private String email;
    private String phonenumber;
    private UserRole userRole ;
    private Cart cart = new Cart();

    // Constructor mặc định

    // Constructor có tham số
    public User(String username, String password, String email, String phonenumber) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phonenumber = phonenumber;
        this.userRole = UserRole.CLIENT; // Mặc định là CLIENT
        this.cart = new Cart(); // Tạo giỏ hàng mới
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }
    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Cart getCart() {
        return cart;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }
}
