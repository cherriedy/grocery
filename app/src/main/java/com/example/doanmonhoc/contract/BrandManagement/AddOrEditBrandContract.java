package com.example.doanmonhoc.contract.BrandManagement;

import android.content.Intent;

import com.example.doanmonhoc.model.Brand;

public class AddOrEditBrandContract {
    public interface View {
        void createBrand();

        void createBrandSuccessfully();

        void createBrandFail();

        void getExtraBrandFail();

        void getExtraBrandSuccessfully(Brand brand);

        void deleteBrand();

        void deleteBrandSuccessfully();

        void deleteBrandFail();

        void updateBrand();

        void onUpdateBrandSuccess();

        void onUpdateBrandFail();
    }

    public interface Presenter {
        void handleCreateBrand(Brand brand);

        void getExtraBrand(Intent intent);

        void handleDeleteBrand(int id);

        void handleUpdateBrand(long id, Brand brand);
    }
}
