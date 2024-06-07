package com.example.doanmonhoc.contract;

import android.content.Intent;

import com.example.doanmonhoc.model.Brand;
import com.example.doanmonhoc.model.Product;
import com.example.doanmonhoc.model.ProductGroup;

import java.util.List;

public interface ProductAddContract {
    interface Presenter {
        void getBrandList();

        void getProductGroupList();

        String generateLatestProductKey();

        void createProduct(Product product);

        void getExtraProduct(Intent intent);
    }

    interface View {
        void getBrandAutoCompleteDataSuccessfully(List<Brand> brandList);

        void getBrandAutoCompleteDataFail();

        void getProductGroupAutoCompleteDataSuccessfully(List<ProductGroup> productGroupList);

        void getProductGroupAutoCompleteDataFail();

        void getBrandItemId();

        void getProductGroupItemId();

        void getNewProductInformation();

        void notifyCreateProductSuccessfully();

        void notifyCreateProductFail();

        void getExtraProductSuccessfully(Product extrasProduct);

        void getExtraProductFail();
    }
}
