package com.example.doanmonhoc.contract.ProductBrandManagement;

import com.example.doanmonhoc.model.Brand;

public class AddProductBrandContract {
    public interface View {
        void createProductBrand();

        void createProductBrandSuccessfully();

        void createProductBrandFail();
    }

    public interface Presenter {
        void handleCreateProductBrand(Brand brand);
    }
}
