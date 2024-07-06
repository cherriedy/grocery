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
import java.util.Optional;

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

    private List<Brand> mBrandList;
    private List<ProductGroup> mGroupList;
    private Product mExtraProduct;
    private Product mLatestProduct;

    public AddOrEditProductPresenter(AddOrEditProductContract.View mView) {
        this.mView = mView;
        mBrandList = new ArrayList<>();
        mGroupList = new ArrayList<>();
        fetchLatestProduct();
    }

    public List<Brand> brandList() {
        return mBrandList;
    }

    public void getBrandList() {
        if (!mBrandList.isEmpty()) {
            mView.getBrandListSuccessfully(mBrandList);
        } else {
            KiotApiService.apiService.getBrandList().enqueue(new Callback<List<Brand>>() {
                @Override
                public void onResponse(Call<List<Brand>> call, Response<List<Brand>> response) {
                    if (response.isSuccessful()) {
                        mBrandList = response.body();
                        mView.getBrandListSuccessfully(mBrandList);
                    } else {
                        mView.getBrandListDataFail();
                    }
                }

                @Override
                public void onFailure(Call<List<Brand>> call, Throwable throwable) {
                    mView.getBrandListDataFail();
                }
            });
        }
    }

    public void getProductGroupList() {
        if (!mGroupList.isEmpty()) {
            mView.getTypeListSuccessfully(mGroupList);
        } else {
            KiotApiService.apiService.getProductGroupList().enqueue(new Callback<List<ProductGroup>>() {
                @Override
                public void onResponse(Call<List<ProductGroup>> call, Response<List<ProductGroup>> response) {
                    if (response.isSuccessful()) {
                        mGroupList.addAll(response.body());
                        mView.getTypeListSuccessfully(mGroupList);
                    }
                }

                @Override
                public void onFailure(Call<List<ProductGroup>> call, Throwable throwable) {
                    mView.getTypeListFail();
                }
            });
        }
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

        mExtraProduct = (Product) intent.getSerializableExtra(ProductManagementActivity.EXTRA_PRODUCT);
        if (mExtraProduct == null) {
            Log.e(TAG, "getExtraProduct: " + "Không có đối tượng Brand truyền vào Intent");
            mView.getExtraProductFail();
            return;
        }

        mView.getExtraProductSuccessfully(mExtraProduct);
    }

    @Override
    public void handleUpdateProduct(Product product) {
        KiotApiService.apiService.updateProduct(mExtraProduct.getId(), product).enqueue(new Callback<Product>() {
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
        if (uniqueKey.isEmpty()) {
            return;
        }
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
        String latestKey = getLatestProductKey();
        if (latestKey.isEmpty()) {
            return "";
        }

        int currentKey = Utils.extraKeyNumber(latestKey, Product.PREFIX);
        if (currentKey == 0) {
            return "";
        }

        return Utils.formatKey(currentKey + 1, Product.PREFIX);
    }

    public String getLatestProductKey() {
        return Optional.ofNullable(mLatestProduct).map(product -> {
            String key = product.getProductKey();
            if (key.isEmpty()) {
                Log.e(TAG, "getLatestProductKey: Product key is empty");
                return "";
            }
            return key;
        }).orElseGet(() -> {
            Log.e(TAG, "getLatestProductKey: mLatestProduct is null");
            return "";
        });
    }

    private void fetchLatestProduct() {
        KiotApiService.apiService.getLatestProduct().enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful()) {
                    mLatestProduct = response.body();
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable throwable) {
                Log.e(TAG, "getCurrentLatestProductKey - onFailure: " + "Lỗi truy vấn api");
            }
        });
    }
}