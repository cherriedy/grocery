package com.example.doanmonhoc.contract.ProductManagement;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.example.doanmonhoc.model.Brand;
import com.example.doanmonhoc.model.Product;
import com.example.doanmonhoc.model.ProductGroup;

import java.util.List;
import java.util.Map;

public interface AddOrEditProductContract {
    interface Presenter {
        void getBrandList();

        void getProductGroupList();

        String generateLatestProductKey();

        void handleCreateProduct(Map<String, Object> productCreationRequest);

        void getExtraProduct(Intent intent);

        void handleUpdateProduct(Product product);

        void handleDeleteProduct(Product product);

        void handleUploadTemporaryImage(Context context, Uri imageUri);
    }

    interface View {
        void getBrandListSuccessfully(List<Brand> brandList);

        void getBrandListDataFail();

        void getTypeListSuccessfully(List<ProductGroup> productGroupList);

        void getTypeListFail();

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

        void uploadTemporaryImage();

        void onUploadTemporaryImageSuccess();

        void onUploadTemporaryImageFail();
    }
}