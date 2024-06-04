package com.example.doanmonhoc.presenter;

import com.example.doanmonhoc.api.KiotApiService;
import com.example.doanmonhoc.model.Product;
import com.example.doanmonhoc.contract.ProductManageContract;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductManagePresenter {
    private final ProductManageContract productManageContract;
    private List<Product> productList;

    public ProductManagePresenter(ProductManageContract productManageContract) {
        this.productManageContract = productManageContract;
        productList = new ArrayList<>();
    }


    public void getProductList() {
        KiotApiService.apiService.getProductList().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                productList = response.body();
                productManageContract.getProductListSuccessfully(productList);
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable throwable) {
                productManageContract.getProductListFail(throwable);
            }
        });

    }
}