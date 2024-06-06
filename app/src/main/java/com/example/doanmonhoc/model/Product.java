package com.example.doanmonhoc.model;

import android.util.Log;

import com.example.doanmonhoc.api.KiotApiService;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Product implements Serializable {

    public static String PREFIX = "PDR";
//    private static Product latestRow = new Product();

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

//    public static String generateProductKey() {
//        int latestNumber = -1, newNumber = -1;
////        String latestProductKey = getLatestProductKey();
////        Log.i("LATEST_PRODUCT_KEY", latestProductKey);
//        String latestProductKey = latestRow.getProductKey();
//
//        if (latestProductKey != null && !latestProductKey.isEmpty()) {
//            latestNumber = extractNumber(latestProductKey);
//            newNumber = latestNumber + 1;
//            Log.i("LATEST_NUMBER", String.valueOf(latestNumber));
//        }
//        return formatProductKey(newNumber);
//    }

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

//    private static int extractNumber(String latestProductKey) {
//        if (latestProductKey.startsWith(PREFIX)) {
//            String numberPart = latestProductKey.substring(PREFIX.length());
//            return Integer.parseInt(numberPart);
//        }
//        return 0;
//    }
//
//    private static String formatProductKey(int newNumber) {
//        return String.format(PREFIX + "%03d", newNumber);
//    }
//
//    private static void getLatestProductKey() {
//        KiotApiService.apiService.getLatestProduct().enqueue(new Callback<Product>() {
//            @Override
//            public void onResponse(Call<Product> call, Response<Product> response) {
//                if (response.isSuccessful()) {
//                    latestRow = response.body();
//                    Log.i("PRODUCT_KEY", latestRow.getProductKey());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Product> call, Throwable throwable) {
//
//            }
//        });
//    }
}


