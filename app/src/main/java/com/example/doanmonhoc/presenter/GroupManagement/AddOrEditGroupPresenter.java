package com.example.doanmonhoc.presenter.GroupManagement;

import android.content.Intent;
import android.util.Log;

import com.example.doanmonhoc.api.KiotApiService;
import com.example.doanmonhoc.contract.GroupManagement.AddOrEditGroupContract;
import com.example.doanmonhoc.model.ProductGroup;
import com.example.doanmonhoc.utils.IntentManager;
import com.example.doanmonhoc.utils.TextUtils;
import com.example.doanmonhoc.utils.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddOrEditGroupPresenter implements AddOrEditGroupContract.Presenter {
    private static final String TAG = AddOrEditGroupPresenter.class.getSimpleName();

    private final AddOrEditGroupContract.View mView;
    private String mLatestGroupKey;
    private ProductGroup mExtraGroup;

    public AddOrEditGroupPresenter(AddOrEditGroupContract.View view) {
        mView = view;
        getLatestGroup();
    }

    @Override
    public void handleExtraGroup(Intent intent) {
        ProductGroup group = (ProductGroup) intent.getSerializableExtra(IntentManager.ExtraParams.EXTRA_GROUP);
        if (group == null) {
            mView.onExtraGroupProcessingFail();
            return;
        }

        mExtraGroup = group;
        mView.onExtraGroupProcessingSuccess(group);
    }

    @Override
    public void handleCreateGroup(ProductGroup group) {
        if (!TextUtils.isValidString(mLatestGroupKey)) {
            return;
        }

        int latestGroupKeyNumber = Utils.extraKeyNumber(mLatestGroupKey, ProductGroup.PREFIX);
        if (latestGroupKeyNumber == 0) {
            return;
        }

        String groupKey = Utils.formatKey(latestGroupKeyNumber + 1, ProductGroup.PREFIX);
        if (!TextUtils.isValidString(groupKey)) {
            return;
        } else {
            group.setProductGroupKey(groupKey);
        }

        group.setProductGroupKey(groupKey);
        KiotApiService.apiService.createProductGroup(group).enqueue(new Callback<ProductGroup>() {
            @Override
            public void onResponse(Call<ProductGroup> call, Response<ProductGroup> response) {
                if (response.isSuccessful()) {
                    mView.onGroupCreationSuccess();
                } else {
                    mView.onGroupCreationFail();
                }
            }

            @Override
            public void onFailure(Call<ProductGroup> call, Throwable throwable) {
                Log.e(TAG, "setProductGroupKey - onFailure: " + "Lỗi truy vấn API");
                mView.onGroupCreationFail();
            }
        });
    }

    @Override
    public void handleUpdateGroup(ProductGroup group) {
        KiotApiService.apiService.updateProductGroup(mExtraGroup.getId(), group).enqueue(new Callback<ProductGroup>() {
            @Override
            public void onResponse(Call<ProductGroup> call, Response<ProductGroup> response) {
                if (response.isSuccessful()) {
                    mView.onGroupUpdateSuccess();
                } else {
                    mView.onGroupUpdateFail();
                }
            }

            @Override
            public void onFailure(Call<ProductGroup> call, Throwable throwable) {
                Log.e(TAG, "handleUpdateGroup - onFailure: " + "Lỗi truy vấn API");
                mView.onGroupUpdateFail();
            }
        });
    }

    @Override
    public void handleDeleteGroup(long id) {
        KiotApiService.apiService.deleteProductGroup(id).enqueue(new Callback<ProductGroup>() {
            @Override
            public void onResponse(Call<ProductGroup> call, Response<ProductGroup> response) {
                if (response.isSuccessful()) {
                    mView.onGroupDeleteSuccess();
                } else {
                    mView.onGroupDeleteFail();
                }
            }

            @Override
            public void onFailure(Call<ProductGroup> call, Throwable throwable) {
                Log.e(TAG, "handleDeleteGroup - onFailure: " + "Lỗi truy vấn API");
                mView.onGroupDeleteFail();
            }
        });
    }

    private void getLatestGroup() {
        KiotApiService.apiService.getLatestProductGroup().enqueue(new Callback<ProductGroup>() {
            @Override
            public void onResponse(Call<ProductGroup> call, Response<ProductGroup> response) {
                if (response.isSuccessful()) {
                    ProductGroup productGroup = response.body();
                    mLatestGroupKey = productGroup.getProductGroupKey();
                } else {
                    mLatestGroupKey = "";
                }
            }

            @Override
            public void onFailure(Call<ProductGroup> call, Throwable throwable) {
                mLatestGroupKey = "";
            }
        });
    }
}
