package com.example.doanmonhoc.model;

import java.io.Serializable;

public class ImportItem {
    private Product product;
    private long productId;
    private int quantity;
    private double price;

    public ImportItem(long productId, int quantity, double price) {
    }

    public ImportItem(Product product, int quantity, float price) {
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
