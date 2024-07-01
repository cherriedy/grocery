package com.example.doanmonhoc.presenter.ProductManagament;

import android.content.Intent;
import android.util.Log;

import com.example.doanmonhoc.activity.ProductManagement.ProductManagementActivity;
import com.example.doanmonhoc.api.KiotApiService;
import com.example.doanmonhoc.contract.ProductManagement.AddOrEditProductContract;
import com.example.doanmonhoc.model.Brand;
import com.example.doanmonhoc.model.DetailedGoodsReceivedNote;
import com.example.doanmonhoc.model.GoodsReceivedNote;
import com.example.doanmonhoc.model.Product;
import com.example.doanmonhoc.model.ProductGroup;
import com.example.doanmonhoc.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddOrEditProductPresenter implements AddOrEditProductContract.Presenter {
    public static final String TAG = "AddOrEditProductPresenter";

    private final AddOrEditProductContract.View view;

    private List<Brand> brandList;
    private List<ProductGroup> productGroupList;
    private String latestProductKey;
    private Product product;

    public AddOrEditProductPresenter(AddOrEditProductContract.View view) {
        this.view = view;
        brandList = new ArrayList<>();
        productGroupList = new ArrayList<>();
    }

    public void getBrandList() {
        KiotApiService.apiService.getBrandList().enqueue(new Callback<List<Brand>>() {
            @Override
            public void onResponse(Call<List<Brand>> call, Response<List<Brand>> response) {
                if (response.isSuccessful()) {
                    brandList = response.body();
                    view.getBrandAutoCompleteDataSuccessfully(brandList);
                } else {
                    view.getBrandAutoCompleteDataFail();
                }
            }

            @Override
            public void onFailure(Call<List<Brand>> call, Throwable throwable) {
                view.getBrandAutoCompleteDataFail();
            }
        });
    }

    public void getProductGroupList() {
        KiotApiService.apiService.getProductGroupList().enqueue(new Callback<List<ProductGroup>>() {
            @Override
            public void onResponse(Call<List<ProductGroup>> call, Response<List<ProductGroup>> response) {
                if (response.isSuccessful()) {
                    productGroupList = response.body();
                    view.getProductGroupAutoCompleteDataSuccessfully(productGroupList);
                }
            }

            @Override
            public void onFailure(Call<List<ProductGroup>> call, Throwable throwable) {
                view.getProductGroupAutoCompleteDataFail();
            }
        });
    }

    public void handleCreateProduct(Product product,
                                    GoodsReceivedNote goodsReceivedNote,
                                    DetailedGoodsReceivedNote detailedGoodsReceivedNote) {
        KiotApiService.apiService.createProduct(product, goodsReceivedNote, detailedGoodsReceivedNote).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful()) {
                    view.createProductSuccessfully();
                } else {
                    view.createProductFail();
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable throwable) {
                Log.e(TAG, "handleCreateProduct - onFailure: " + "Lỗi truy vấn api");
                view.createProductFail();
            }
        });
    }

    @Override
    public void getExtraProduct(Intent intent) {
        if (intent == null) {
            Log.e(TAG, "getExtraProduct: " + "intent truyền vào là null");
            view.getExtraProductFail();
            return;
        }

        product = (Product) intent.getSerializableExtra(ProductManagementActivity.EXTRA_PRODUCT);
        if (product == null) {
            Log.e(TAG, "getExtraProduct: " + "Không có đối tượng Brand truyền vào Intent");
            view.getExtraProductFail();
            return;
        }

        view.getExtraProductSuccessfully(product);
    }

    @Override
    public void handleUpdateProduct(Product product) {
        KiotApiService.apiService.updateProduct(this.product.getId(), product).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful()) {
                    view.updateProductSuccessfully();
                } else {
                    view.updateProductFail();
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable throwable) {
                Log.e("UpdateProduct", throwable.getMessage());
                view.updateProductFail();
            }
        });
    }

    @Override
    public void handleDeleteProduct(Product product) {
        KiotApiService.apiService.deleteProduct(product.getId()).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful()) {
                    view.deleteProductSuccessfully();
                } else {
                    Log.e(TAG, "handleDeleteProduct - onResponse: " + "Lỗi xử lý dữ liệu ở API");
                    view.deleteProductFail();
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable throwable) {
                Log.e(TAG, "handleDeleteProduct - onFailure: " + "Lỗi truy vấn API");
                view.deleteProductFail();
            }
        });
    }

    public String generateLatestProductKey() {
        int currentKey = Utils.extraKeyNumber(latestProductKey, Product.PREFIX);
        return Utils.formatKey(currentKey + 1, Product.PREFIX);
    }

    public void getCurrentLatestProductKey() {
        KiotApiService.apiService.getLatestProduct().enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful()) {
                    Product product = response.body();
                    latestProductKey = product.getProductKey();
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable throwable) {
                Log.e(TAG, "getCurrentLatestProductKey - onFailure: " + "Lỗi truy vấn api");
            }
        });
    }
}