package com.example.backendshop.model;

import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    private List<CartItem> items = new ArrayList<>();

    public void addItem(CartItem item) {
        for (CartItem cartItem : items) {
            if (cartItem.getProductId().equals(item.getProductId())) {
                cartItem.setQuantity(cartItem.getQuantity() + item.getQuantity()); // Cộng dồn số lượng nếu sản phẩm đã có
                return;
            }
        }
        items.add(item); // Nếu sản phẩm chưa có, thêm mới
    }

    public void removeItem(String productId) {
        items.removeIf(item -> item.getProductId().equals(productId));
    }

    public double getTotalPrice() {
        return items.stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();
    }
}
