package com.example.doanmonhoc.contract.ProductManagement;

import com.example.doanmonhoc.model.Product;

import java.util.List;

public interface ProductManageContract {

    interface View {
        void getProductListSuccessfully(List<Product> productList);

        void getProductListFail();
    }

    interface Presenter {
        void getProductList();
    }
}
