package com.example.doanmonhoc.model;

public class DetailedGoodsReceivedNote {
    private long id;
    private long GoodReceivedNoteid;
    private long productId;
    private int quantity;
    private float price;
    private Product product;

    public DetailedGoodsReceivedNote() {
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

    public float getPrice() {
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