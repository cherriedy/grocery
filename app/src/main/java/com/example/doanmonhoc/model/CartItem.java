package com.example.doanmonhoc.model;

import java.io.Serializable;

public class CartItem implements Serializable {
    private Product product;
    private long productId;
    private int quantity;
    private double price;



    public CartItem(long productId, int quantity, double price) {
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    public CartItem(Product product, int quantity, double price) {
        this.product = product;
        this.quantity = quantity;
        this.price = price;

        this.productId = product.getId(); // Lấy productId từ đối tượng Product


    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
