package com.example.doanmonhoc.presenter;

import android.content.Intent;

import androidx.activity.result.ActivityResultLauncher;

import com.example.doanmonhoc.api.KiotApiService;
import com.example.doanmonhoc.contract.ProductManageContract;
import com.example.doanmonhoc.model.Product;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductManagePresenter implements ProductManageContract.Presenter{
    private List<Product> productList;
    private ActivityResultLauncher<Intent> startDetailedProductActivityIntent;
    private final ProductManageContract.View productManageViewContract;

    public ProductManagePresenter(ProductManageContract.View productManageViewContract) {
        this.productManageViewContract = productManageViewContract;
        productList = new ArrayList<>();
    }

    public void getProductList() {
        KiotApiService.apiService.getProductList().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                productList = response.body();
                productManageViewContract.getProductListSuccessfully(productList);
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable throwable) {
                productManageViewContract.getProductListFail(throwable);
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