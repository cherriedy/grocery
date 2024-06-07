package com.example.doanmonhoc.contract;

import com.example.doanmonhoc.model.Product;

import java.util.List;

public interface ProductManageContract {

    interface View {
        void getProductListSuccessfully(List<Product> productList);
        void getProductListFail(Throwable throwable);
    }

    interface Presenter {
        void getProductList();
    }
}
