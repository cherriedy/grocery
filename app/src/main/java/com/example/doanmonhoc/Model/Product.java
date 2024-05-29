package com.example.doanmonhoc.Model;

public class Product {
    private long id;
    private String productKey;
    private String productBarcode;
    private String productName;
    private float inPrice;
    private float outPrice;
    private String avatarPath;
//    test
    private int resource;
    private short inventoryQuantity;
    private short actuallyQuantity;
    private String description;
    private boolean status;
    private float discount;
    private String note;
    private long productGroupId;
    private long productBrandId;

    public Product() {
    }

    public Product(String productName, float outPrice, int resource) {
        this.productName = productName;
        this.outPrice = outPrice;
        this.resource = resource;
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

    public short getInventoryQuantity() {
        return inventoryQuantity;
    }

    public void setInventoryQuantity(short inventoryQuantity) {
        this.inventoryQuantity = inventoryQuantity;
    }

    public short getActuallyQuantity() {
        return actuallyQuantity;
    }

    public void setActuallyQuantity(short actuallyQuantity) {
        this.actuallyQuantity = actuallyQuantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
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

    public void setNote(String note) {
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

    public int getResource() {
        return resource;
    }

    public void setResource(int resource) {
        this.resource = resource;
    }
}
