package com.example.doanmonhoc.model;

public class ProductGroup {
    public static final String PREFIX = "PG";

    private int id;
    private String productGroupKey;
    private String productGroupName;

    public ProductGroup() {
    }

    public ProductGroup(int id, String productGroupKey, String productGroupName) {
        this.id = id;
        this.productGroupKey = productGroupKey;
        this.productGroupName = productGroupName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductGroupKey() {
        return productGroupKey;
    }

    public void setProductGroupKey(String productGroupKey) {
        this.productGroupKey = productGroupKey;
    }

    public String getProductGroupName() {
        return productGroupName;
    }

    public void setProductGroupName(String productGroupName) {
        this.productGroupName = productGroupName;
    }
}
