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
            if (cartItem.getProductId().equals(item.getProductId()) &&
                    cartItem.getSize().equals(item.getSize())) {
                cartItem.setQuantity(cartItem.getQuantity() + item.getQuantity());
                return;
            }
        }
        items.add(item);
    }
    public void removeItem(String productId, String size) {
        items.removeIf(item ->
                        item.getProductId().equals(productId) && item.getSize().equals(size));
    }

    public void increaseQuantity(String productId, String size) {
        for (CartItem item : items) {
            if (item.getProductId().equals(productId) && item.getSize().equals(size)) {
                item.setQuantity(item.getQuantity() + 1);
                return;
            }
        }
    }

    public void decreaseQuantity(String productId, String size) {
        for (CartItem item : items) {
            if (item.getProductId().equals(productId) && item.getSize().equals(size)) {
                int currentQty = item.getQuantity();
                if (currentQty > 1) {
                    item.setQuantity(currentQty - 1);
                } else {
                    removeItem(productId, size); // Xoá luôn nếu còn 1
                }
                return;
            }
        }
    }

    public double getTotalPrice() {
        return items.stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();
    }
}
