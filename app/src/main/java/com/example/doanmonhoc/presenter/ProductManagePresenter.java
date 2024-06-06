package com.example.doanmonhoc.presenter;

import android.content.Context;
import android.content.Intent;

import androidx.activity.result.ActivityResultLauncher;

import com.example.doanmonhoc.activity.ProductManagement.AddProductActivity;
import com.example.doanmonhoc.adapter.ProductRecyclerViewAdapter;
import com.example.doanmonhoc.api.KiotApiService;
import com.example.doanmonhoc.contract.ProductManageContract;
import com.example.doanmonhoc.model.Product;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductManagePresenter {
    private List<Product> productList;
    private ActivityResultLauncher<Intent> startDetailedProductActivityIntent;
    private final ProductManageContract productManageContract;

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

//    public void handleOnItemClick(Context context, Product product) {
//        ProductRecyclerViewAdapter.OnItemClickListener listener = new ProductRecyclerViewAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick() {
//                Intent intent = new Intent(context, AddProductActivity.class);
//                intent.putExtra(EXTRA_PRODUCT, (Serializable) product);
//            }
//        };
//    }
}