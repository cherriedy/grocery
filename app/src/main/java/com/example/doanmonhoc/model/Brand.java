package com.example.doanmonhoc.model;

public class Brand {
    public static final String PREFIX = "BR";

    private int id;
    private String brandKey;
    private String brandName;

    public Brand() {
    }

    public Brand(String brandName) {
        this.brandName = brandName;
    }

    public Brand(int id, String brandKey, String brandName) {
        this.id = id;
        this.brandKey = brandKey;
        this.brandName = brandName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBrandKey() {
        return brandKey;
    }

    public void setBrandKey(String brandKey) {
        this.brandKey = brandKey;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }
}
