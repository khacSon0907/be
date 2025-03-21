package com.example.backendshop.model;


import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    private String productId;
    private String productName;
    private int quantity;
    private double price;

    public double getPrice(){
        return price;
    }
    public void setPrice(double price){
        this.price = price;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }
}
