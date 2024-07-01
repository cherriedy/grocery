package com.example.doanmonhoc.contract.ProductManagement;

import android.content.Intent;

import com.example.doanmonhoc.model.Brand;
import com.example.doanmonhoc.model.DetailedGoodsReceivedNote;
import com.example.doanmonhoc.model.GoodsReceivedNote;
import com.example.doanmonhoc.model.Product;
import com.example.doanmonhoc.model.ProductGroup;

import java.util.List;

public interface AddOrEditProductContract {
    interface Presenter {
        void getBrandList();

        void getProductGroupList();

        String generateLatestProductKey();

        void handleCreateProduct(Product product, GoodsReceivedNote goodsReceivedNote, DetailedGoodsReceivedNote detailedGoodsReceivedNote);

        void getExtraProduct(Intent intent);

        void handleUpdateProduct(Product product);

        void handleDeleteProduct(Product product);
    }

    interface View {
        void getBrandAutoCompleteDataSuccessfully(List<Brand> brandList);

        void getBrandAutoCompleteDataFail();

        void getProductGroupAutoCompleteDataSuccessfully(List<ProductGroup> productGroupList);

        void getProductGroupAutoCompleteDataFail();

        void getBrandItemId();

        void getProductGroupItemId();

        void createProduct();

        void createProductSuccessfully();

        void createProductFail();

        void getExtraProductSuccessfully(Product product);

        void deleteProduct();

        void getExtraProductFail();

        void deleteProductSuccessfully();

        void deleteProductFail();

        void updateProduct();

        void updateProductSuccessfully();

        void updateProductFail();
    }
}
