package com.example.doanmonhoc.presenter.BrandManagement;

import android.util.Log;

import com.example.doanmonhoc.api.KiotApiService;
import com.example.doanmonhoc.contract.BrandManagement.ProductBrandManageContract;
import com.example.doanmonhoc.model.Brand;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BrandManagePresenter implements ProductBrandManageContract.Presenter {
    private static String TAG = "ProductBrandManagePresenter";
    private final ProductBrandManageContract.View productBrandManageViewContract;
    private List<Brand> brandList;

    public BrandManagePresenter(ProductBrandManageContract.View productBrandManageViewContract) {
        this.productBrandManageViewContract = productBrandManageViewContract;
    }

    public void getProductBrandList() {
        KiotApiService.apiService.getBrandList().enqueue(new Callback<List<Brand>>() {
            @Override
            public void onResponse(Call<List<Brand>> call, Response<List<Brand>> response) {
                if (response.isSuccessful()) {
                    if (!response.body().isEmpty()) {
                        brandList = response.body();
                        productBrandManageViewContract.getProductBrandListSuccessfully(brandList);
                    } else {
                        productBrandManageViewContract.getProductBrandListFail();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Brand>> call, Throwable throwable) {
                Log.e(TAG, throwable.getMessage());
                productBrandManageViewContract.getProductBrandListFail();
            }
        });
    }
}
