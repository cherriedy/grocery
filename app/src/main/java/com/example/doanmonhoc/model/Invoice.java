package com.example.doanmonhoc.model;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class Invoice {
    private long id;
    private String invoiceKey, note;
    private long Staffid;
    private Timestamp createdAt;
    private double totalAmount;
    private List<CartItem> cartItems;

    public Invoice() {
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getInvoiceKey() {
        return invoiceKey;
    }

    public void setInvoiceKey(String invoiceKey) {
        this.invoiceKey = invoiceKey;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }


    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }


    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public long getStaffid() {
        return Staffid;
    }

    public void setStaffid(long staffid) {
        Staffid = staffid;
    }
}
