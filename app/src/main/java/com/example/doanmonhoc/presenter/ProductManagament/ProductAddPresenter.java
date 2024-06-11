package com.example.doanmonhoc.presenter.ProductManagament;

import android.content.Intent;
import android.util.Log;

import com.example.doanmonhoc.activity.ProductManagement.ProductManagementActivity;
import com.example.doanmonhoc.api.KiotApiService;
import com.example.doanmonhoc.contract.ProductManagement.ProductAddContract;
import com.example.doanmonhoc.databinding.ActivityAddProductBinding;
import com.example.doanmonhoc.model.Brand;
import com.example.doanmonhoc.model.Product;
import com.example.doanmonhoc.model.ProductGroup;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductAddPresenter implements ProductAddContract.Presenter {

    private List<Brand> brandList;
    private List<ProductGroup> productGroupList;
    private ActivityAddProductBinding b;
    private final ProductAddContract.View productAddViewContract;
    private String currentLatestProductKey;
    public static String CREATE_TAG = "CREATE_PRODUCT";

    public ProductAddPresenter(ProductAddContract.View productAddViewContract) {
        this.productAddViewContract = productAddViewContract;
        brandList = new ArrayList<>();
        productGroupList = new ArrayList<>();
    }

    public void getBrandList() {
        KiotApiService.apiService.getBrandList().enqueue(new Callback<List<Brand>>() {
            @Override
            public void onResponse(Call<List<Brand>> call, Response<List<Brand>> response) {
                if (response.isSuccessful()) {
                    brandList = response.body();
                    productAddViewContract.getBrandAutoCompleteDataSuccessfully(brandList);
                }
            }

            @Override
            public void onFailure(Call<List<Brand>> call, Throwable throwable) {
                productAddViewContract.getBrandAutoCompleteDataFail();
            }
        });
    }

    public void getProductGroupList() {
        KiotApiService.apiService.getProductGroupList().enqueue(new Callback<List<ProductGroup>>() {
            @Override
            public void onResponse(Call<List<ProductGroup>> call, Response<List<ProductGroup>> response) {
                if (response.isSuccessful()) {
                    productGroupList = response.body();
                    productAddViewContract.getProductGroupAutoCompleteDataSuccessfully(productGroupList);
                }
            }

            @Override
            public void onFailure(Call<List<ProductGroup>> call, Throwable throwable) {
                productAddViewContract.getProductGroupAutoCompleteDataFail();
            }
        });
    }

    public void createProduct(Product product) {

        KiotApiService.apiService.createProduct(product).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful()) {
                    productAddViewContract.notifyCreateProductSuccessfully();
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable throwable) {
                Log.e(CREATE_TAG, throwable.getMessage());
                productAddViewContract.notifyCreateProductFail();
            }
        });
    }

    @Override
    public void getExtraProduct(Intent intent) {
        if (intent != null) {
            Product receivedExtraProduct = (Product) intent.getSerializableExtra(ProductManagementActivity.EXTRA_PRODUCT);
            if (receivedExtraProduct != null) {
                productAddViewContract.getExtraProductSuccessfully(receivedExtraProduct);
            }
        }
        productAddViewContract.getExtraProductFail();
    }

    public String generateLatestProductKey() {
//        getCurrentLatestProductKey();
        Log.i("CURRENT_PD+KEY", currentLatestProductKey);
        int currentKey = parseProductKey(currentLatestProductKey);
        return formatProductKey(currentKey + 1);
    }

    public void getCurrentLatestProductKey() {
        KiotApiService.apiService.getLatestProduct().enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful()) {
                    Product product = response.body();
                    currentLatestProductKey = product.getProductKey();
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable throwable) {
                Log.e("GET_PRODUCT_LATEST_KEY", throwable.getMessage());
            }
        });
    }

    private String formatProductKey(int key) {
        return String.format(Product.PREFIX + "%03d", key);
    }

    private int parseProductKey(String latestProductKey) {
//        if (latestProductKey.startsWith(Product.PREFIX)) {
            String keyText = latestProductKey.substring(Product.PREFIX.length());
            return Integer.parseInt(keyText);
//        }
//        return -1;
    }
}
