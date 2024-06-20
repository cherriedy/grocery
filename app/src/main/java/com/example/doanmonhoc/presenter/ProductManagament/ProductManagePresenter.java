package com.example.doanmonhoc.presenter.ProductManagament;

import android.util.Log;

import com.example.doanmonhoc.api.KiotApiService;
import com.example.doanmonhoc.contract.ProductManagement.ProductManageContract;
import com.example.doanmonhoc.model.Product;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductManagePresenter implements ProductManageContract.Presenter {
    private final static String TAG = "ProductManagePresenter";
    private final ProductManageContract.View view;
    private List<Product> productList;

    public ProductManagePresenter(ProductManageContract.View view) {
        this.view = view;
        productList = new ArrayList<>();
    }

    @Override
    public void getProductList() {
        KiotApiService.apiService.getProductList().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                productList = response.body();
                view.getProductListSuccessfully(productList);
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable throwable) {
                Log.e(TAG, "getProductList - onFailure: " + "Lỗi truy vấn api");
                view.getProductListFail();
            }
        });
    }
}