package com.example.doanmonhoc.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Product implements Serializable {
    public static final String PREFIX = "PRD";

    private long id;
    private String productKey;
    private String productBarcode;
    private String productName;
    private float inPrice;
    private float outPrice;
    private String avatarPath;
    private int inventoryQuantity;
    private int actualQuantity;
    private String description;
    private byte status;
    private float discount;
    private String note;

    @SerializedName("ProductGroupid")
    private long productGroupId;

    @SerializedName("brandOfProductid")
    private long productBrandId;

    public Product() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public String getProductBarcode() {
        return productBarcode;
    }

    public void setProductBarcode(String productBarcode) {
        this.productBarcode = productBarcode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public float getInPrice() {
        return inPrice;
    }

    public void setInPrice(float inPrice) {
        this.inPrice = inPrice;
    }

    public float getOutPrice() {
        return outPrice;
    }

    public void setOutPrice(float outPrice) {
        this.outPrice = outPrice;
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    public int getInventoryQuantity() {
        return inventoryQuantity;
    }

    public void setInventoryQuantity(int inventoryQuantity) {
        this.inventoryQuantity = inventoryQuantity;
    }

    public int getActualQuantity() {
        return actualQuantity;
    }

    public void setActualQuantity(int actualQuantity) {
        this.actualQuantity = actualQuantity;
    }

    public byte getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte isStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public String getNote() {
        return note;
    }

    public void setProductNote(String note) {
        this.note = note;
    }

    public long getProductGroupId() {
        return productGroupId;
    }

    public void setProductGroupId(long productGroupId) {
        this.productGroupId = productGroupId;
    }

    public long getProductBrandId() {
        return productBrandId;
    }

    public void setProductBrandId(long productBrandId) {
        this.productBrandId = productBrandId;
    }

    @NonNull
    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", productKey='" + productKey + '\'' +
                ", productBarcode='" + productBarcode + '\'' +
                ", productName='" + productName + '\'' +
                ", inPrice=" + inPrice +
                ", outPrice=" + outPrice +
                ", avatarPath='" + avatarPath + '\'' +
                ", inventoryQuantity=" + inventoryQuantity +
                ", actualQuantity=" + actualQuantity +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", discount=" + discount +
                ", note='" + note + '\'' +
                ", productGroupId=" + productGroupId +
                ", productBrandId=" + productBrandId +
                '}';
    }
}


