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

    public double getTotalPrice() {
        return items.stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();
    }
}
