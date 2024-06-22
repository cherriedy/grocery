package com.example.doanmonhoc.presenter.BrandManagement;

import android.content.Intent;
import android.util.Log;

import com.example.doanmonhoc.api.KiotApiService;
import com.example.doanmonhoc.contract.BrandManagement.AddOrEditBrandContract;
import com.example.doanmonhoc.model.Brand;
import com.example.doanmonhoc.utils.IntentManager;
import com.example.doanmonhoc.utils.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddOrEditBrandPresenter implements AddOrEditBrandContract.Presenter {
    private static final String TAG = "AddBrandPresenter";

    private final AddOrEditBrandContract.View view;
    private Brand latestBrand;

    public AddOrEditBrandPresenter(AddOrEditBrandContract.View view) {
        this.view = view;
        getLatestProductBrandKey();
    }

    @Override
    public void handleCreateBrand(Brand brand) {
        if (latestBrand == null || brand == null) {
            Log.e(TAG, "handleCreateBrand: " + "latestProductBrand hoặc brand là null");
            view.createBrandFail();
            return;
        }

        int latestBrandNumberKey = Utils.extraKeyNumber(latestBrand.getBrandKey(), Brand.PREFIX);
        if (latestBrandNumberKey == 0) {
            Log.e(TAG, "handleCreateBrand: " + "Lỗi trích xuất latestProductBrand");
            return;
        }

        String newProductBrandKey = null;
        newProductBrandKey = Utils.formatKey(latestBrandNumberKey + 1, Brand.PREFIX);
        if (newProductBrandKey.isEmpty()) {
            Log.e(TAG, "handleCreateBrand: " + "Lỗi format newProductBrandKey");
            return;
        } else {
            brand.setBrandKey(newProductBrandKey);
        }

        KiotApiService.apiService.createBrand(brand).enqueue(new Callback<Brand>() {
            @Override
            public void onResponse(Call<Brand> call, Response<Brand> response) {
                if (response.isSuccessful()) {
                    view.createBrandSuccessfully();
                } else {
                    view.createBrandFail();
                }
            }

            @Override
            public void onFailure(Call<Brand> call, Throwable throwable) {
                view.createBrandFail();
                Log.e(TAG, "HandleCreateProductBrand - onFailure: " + "Lỗi truy vấn api");
            }
        });
    }

    @Override
    public void getExtraBrand(Intent intent) {
        if (intent == null) {
            Log.e(TAG, "getExtraBrand: " + "intent truyền vào là null");
            view.getExtraBrandFail();
            return;
        }

        Brand brand = (Brand) intent.getSerializableExtra(IntentManager.ExtraParams.EXTRA_BRAND);
        if (brand == null) {
            Log.e(TAG, "getExtraBrand: " + "Không có đối tượng Brand truyền qua Intent");
            view.getExtraBrandFail();
            return;
        }

        view.getExtraBrandSuccessfully(brand);
    }

    @Override
    public void handleDeleteBrand(int id) {
        KiotApiService.apiService.deleteBrand(id).enqueue(new Callback<Brand>() {
            @Override
            public void onResponse(Call<Brand> call, Response<Brand> response) {
                if (response.isSuccessful()) {
                    view.deleteBrandSuccessfully();
                } else {
                    view.deleteBrandFail();
                }
            }

            @Override
            public void onFailure(Call<Brand> call, Throwable throwable) {
                Log.e(TAG, "handleDeleteBrand - onFailure: " + "Lỗi truy vấn api");
                view.deleteBrandFail();
            }
        });
    }

    private void getLatestProductBrandKey() {
        KiotApiService.apiService.getLatestProductBrand().enqueue(new Callback<Brand>() {
            @Override
            public void onResponse(Call<Brand> call, Response<Brand> response) {
                if (response.isSuccessful()) {
                    latestBrand = response.body();
                } else {
                    latestBrand = null;
                    Log.e(TAG, "getLatestProductKey - onResponse: " + "Không truy vấn được Product mới nhất (latest product)");
                }
            }

            @Override
            public void onFailure(Call<Brand> call, Throwable throwable) {
                Log.e(TAG, "onFailure: " + throwable.getMessage());
            }
        });
    }
}
