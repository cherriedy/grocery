package com.example.doanmonhoc.presenter.ProductManagament;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.example.doanmonhoc.activity.ProductManagement.ProductManagementActivity;
import com.example.doanmonhoc.api.KiotApiService;
import com.example.doanmonhoc.contract.ProductManagement.AddOrEditProductContract;
import com.example.doanmonhoc.model.Brand;
import com.example.doanmonhoc.model.Product;
import com.example.doanmonhoc.model.ProductGroup;
import com.example.doanmonhoc.utils.RealPathUtils;
import com.example.doanmonhoc.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddOrEditProductPresenter implements AddOrEditProductContract.Presenter {
    public static final String TAG = "AddOrEditProductPresenter";

    private final AddOrEditProductContract.View mView;

    private List<Brand> brandList;
    private List<ProductGroup> productGroupList;
    private String latestProductKey;
    private Product product;

    public AddOrEditProductPresenter(AddOrEditProductContract.View mView) {
        this.mView = mView;
        brandList = new ArrayList<>();
        productGroupList = new ArrayList<>();
    }

    public void getBrandList() {
        KiotApiService.apiService.getBrandList().enqueue(new Callback<List<Brand>>() {
            @Override
            public void onResponse(Call<List<Brand>> call, Response<List<Brand>> response) {
                if (response.isSuccessful()) {
                    brandList = response.body();
                    mView.getBrandAutoCompleteDataSuccessfully(brandList);
                } else {
                    mView.getBrandAutoCompleteDataFail();
                }
            }

            @Override
            public void onFailure(Call<List<Brand>> call, Throwable throwable) {
                mView.getBrandAutoCompleteDataFail();
            }
        });
    }

    public void getProductGroupList() {
        KiotApiService.apiService.getProductGroupList().enqueue(new Callback<List<ProductGroup>>() {
            @Override
            public void onResponse(Call<List<ProductGroup>> call, Response<List<ProductGroup>> response) {
                if (response.isSuccessful()) {
                    productGroupList = response.body();
                    mView.getProductGroupAutoCompleteDataSuccessfully(productGroupList);
                }
            }

            @Override
            public void onFailure(Call<List<ProductGroup>> call, Throwable throwable) {
                mView.getProductGroupAutoCompleteDataFail();
            }
        });
    }

    public void handleCreateProduct(Map<String, Object> productCreationRequest) {
        KiotApiService.apiService.createProduct(productCreationRequest).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful()) {
                    mView.createProductSuccessfully();
                } else {
                    mView.createProductFail();
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable throwable) {
                Log.e(TAG, "handleCreateProduct - onFailure: " + "Lỗi truy vấn api");
                Log.e(TAG, "onFailure: " + throwable.getMessage());
                mView.createProductFail();
            }
        });
    }

    @Override
    public void getExtraProduct(Intent intent) {
        if (intent == null) {
            Log.e(TAG, "getExtraProduct: " + "intent truyền vào là null");
            mView.getExtraProductFail();
            return;
        }

        product = (Product) intent.getSerializableExtra(ProductManagementActivity.EXTRA_PRODUCT);
        if (product == null) {
            Log.e(TAG, "getExtraProduct: " + "Không có đối tượng Brand truyền vào Intent");
            mView.getExtraProductFail();
            return;
        }

        mView.getExtraProductSuccessfully(product);
    }

    @Override
    public void handleUpdateProduct(Product product) {
        KiotApiService.apiService.updateProduct(this.product.getId(), product).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful()) {
                    mView.updateProductSuccessfully();
                } else {
                    mView.updateProductFail();
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable throwable) {
                Log.e("UpdateProduct", throwable.getMessage());
                mView.updateProductFail();
            }
        });
    }

    @Override
    public void handleDeleteProduct(Product product) {
        KiotApiService.apiService.deleteProduct(product.getId()).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful()) {
                    mView.deleteProductSuccessfully();
                } else {
                    Log.e(TAG, "handleDeleteProduct - onResponse: " + "Lỗi xử lý dữ liệu ở API");
                    mView.deleteProductFail();
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable throwable) {
                Log.e(TAG, "handleDeleteProduct - onFailure: " + "Lỗi truy vấn API");
                mView.deleteProductFail();
            }
        });
    }

    @Override
    public void handleUploadTemporaryImage(Context context, Uri imageUri) {
        String uniqueKey = generateLatestProductKey();
        String path = RealPathUtils.getRealPath(context, imageUri);
        Log.d(TAG, "handleUploadTemporaryImage: " + "ImagePath: " + path);

        File thumbnail = new File(path);
        RequestBody requestBodyThumbnail =
                RequestBody.create(MediaType.parse("multipart/form-data"), thumbnail);
        RequestBody requestBodyUniqueKey =
                RequestBody.create(MediaType.parse("multipart/form-data"), uniqueKey);
        MultipartBody.Part partBodyThumbnail =
                MultipartBody.Part.createFormData("image", thumbnail.getName(), requestBodyThumbnail);

        KiotApiService.apiService
                .pushTemporaryFiles(requestBodyUniqueKey, partBodyThumbnail)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            mView.onUploadTemporaryImageSuccess();
                        } else {
                            Log.e(TAG, "handleUploadTemporaryImage - onResponse: " + "Lỗi xử lí ở API");
                            mView.onUploadTemporaryImageFail();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                        Log.e(TAG, "handleUploadTemporaryImage - OnFailure: " + "Lỗi truy vấn API");
                        mView.onUploadTemporaryImageFail();
                    }
                });
    }

    public String generateLatestProductKey() {
        int currentKey = Utils.extraKeyNumber(latestProductKey, Product.PREFIX);
        return Utils.formatKey(currentKey + 1, Product.PREFIX);
    }

    public void getCurrentLatestProductKey() {
        KiotApiService.apiService.getLatestProduct().enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful()) {
                    Product product = response.body();
                    latestProductKey = product.getProductKey();
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable throwable) {
                Log.e(TAG, "getCurrentLatestProductKey - onFailure: " + "Lỗi truy vấn api");
            }
        });
    }
}