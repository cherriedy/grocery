package com.example.doanmonhoc.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;


public class GoodsReceivedNote {
    private long id;
    private String grnKey;
    private long Staffid;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private double totalAmount;
    private List<CartItem> cartItems;

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    private String note;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGrnKey() {
        return grnKey;
    }

    public void setGrnKey(String grnKey) {
        this.grnKey = grnKey;
    }

    public long getStaffid() {
        return Staffid;
    }

    public void setStaffid(long staffid) {
        Staffid = staffid;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
