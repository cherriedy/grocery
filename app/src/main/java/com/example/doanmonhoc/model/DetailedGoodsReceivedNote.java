package com.example.doanmonhoc.model;

import java.io.Serializable;

public class DetailedGoodsReceivedNote implements Serializable {
    private long id;
    private long GoodReceivedNoteid;
    private long productId;
    private int quantity;
    private double price;
    private Product product;

    public DetailedGoodsReceivedNote() {
    }

    public DetailedGoodsReceivedNote(long productId, int quantity, double price) {
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    public DetailedGoodsReceivedNote(Product product, int quantity, double price) {
        this.product = product;
        this.quantity = quantity;
        this.price = price;
        this.productId = product.getId(); // Lấy productId từ đối tượng Product
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getGoodReceivedNoteid() {
        return GoodReceivedNoteid;
    }

    public void setGoodReceivedNoteid(long goodReceivedNoteid) {
        GoodReceivedNoteid = goodReceivedNoteid;
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

    public void setPrice(float price) {
        this.price = price;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}