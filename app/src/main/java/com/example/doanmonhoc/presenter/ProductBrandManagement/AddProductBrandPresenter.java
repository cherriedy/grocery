package com.example.doanmonhoc.presenter.ProductBrandManagement;

import android.util.Log;

import com.example.doanmonhoc.api.KiotApiService;
import com.example.doanmonhoc.contract.ProductBrandManagement.AddProductBrandContract;
import com.example.doanmonhoc.model.Brand;
import com.example.doanmonhoc.utils.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProductBrandPresenter implements AddProductBrandContract.Presenter {
    private Brand latestProductBrand;
    private final AddProductBrandContract.View addProductBrandViewContract;

    public AddProductBrandPresenter(AddProductBrandContract.View addProductBrandViewContract) {
        this.addProductBrandViewContract = addProductBrandViewContract;
        getLatestProductBrandKey();
    }

    @Override
    public void handleCreateProductBrand(Brand brand) {
        if (latestProductBrand == null || brand == null) {
            Log.e("HandleCreateProductBrand", "latestProductBrand hoặc brand là null");
            addProductBrandViewContract.createProductBrandFail();
            return;
        }

        int latestProductBrandNumberKey = Utils.extraKeyNumber(latestProductBrand.getBrandKey(), Brand.PREFIX);
        if (latestProductBrandNumberKey == 0) {
            Log.e("HandleCreateProductBrand", "Lỗi trích xuất latestProductBrand");
            return;
        }

        String newProductBrandKey = null;
        newProductBrandKey = Utils.formatKey(latestProductBrandNumberKey + 1, Brand.PREFIX);
        if (newProductBrandKey.isEmpty()) {
            Log.e("HandleCreateProductBrand", "Lỗi format newProductBrandKey");
            return;
        } else {
            Log.i("ProductBrandKey", newProductBrandKey);
            brand.setBrandKey(newProductBrandKey);
        }

        KiotApiService.apiService.createBrand(brand).enqueue(new Callback<Brand>() {
            @Override
            public void onResponse(Call<Brand> call, Response<Brand> response) {
                if (response.isSuccessful()) {
                    addProductBrandViewContract.createProductBrandSuccessfully();
                } else {
                    addProductBrandViewContract.createProductBrandFail();
                }
            }

            @Override
            public void onFailure(Call<Brand> call, Throwable throwable) {
                addProductBrandViewContract.createProductBrandFail();
                Log.e("HandleCreateProductBrand", throwable.getMessage());
            }
        });
    }

    private void getLatestProductBrandKey() {
        KiotApiService.apiService.getLatestProductBrand().enqueue(new Callback<Brand>() {
            @Override
            public void onResponse(Call<Brand> call, Response<Brand> response) {
                if (response.isSuccessful()) {
                    latestProductBrand = response.body();
                } else {
                    latestProductBrand = null;
                    Log.e("LatestProductBrandKey", "Không có dữ liệu");
                }
            }

            @Override
            public void onFailure(Call<Brand> call, Throwable throwable) {
                Log.e("LatestProductBrandKey", "Lỗi truy vấn, " + throwable.getMessage());
            }
        });
    }
}
