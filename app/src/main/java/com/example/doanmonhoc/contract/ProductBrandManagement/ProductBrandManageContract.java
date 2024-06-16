package com.example.doanmonhoc.contract.ProductBrandManagement;

import android.content.Context;

import com.example.doanmonhoc.model.Brand;

import java.util.List;

public interface ProductBrandManageContract {
    public interface Presenter {
        void getProductBrandList();
    }

    public interface View {
        void getProductBrandListSuccessfully(List<Brand> brandList);

        void getProductBrandListFail();
    }
}
