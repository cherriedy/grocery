package com.example.doanmonhoc.presenter.GroupManagement;

import android.util.Log;

import com.example.doanmonhoc.api.KiotApiService;
import com.example.doanmonhoc.contract.GroupManagement.GroupManagementContract;
import com.example.doanmonhoc.model.ProductGroup;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupManagementPresenter implements GroupManagementContract.Presenter {
    private static final String TAG = "GroupManagementPresenter";

    private List<ProductGroup> mGroupList;
    private final GroupManagementContract.View mView;

    public GroupManagementPresenter(GroupManagementContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void fetchGroupList() {
        KiotApiService.apiService.getProductGroupList().enqueue(new Callback<List<ProductGroup>>() {
            @Override
            public void onResponse(Call<List<ProductGroup>> call, Response<List<ProductGroup>> response) {
                if (response.isSuccessful()) {
                    mGroupList = response.body();
                    mView.onGroupListFetchSuccess(mGroupList);
                }
            }

            @Override
            public void onFailure(Call<List<ProductGroup>> call, Throwable throwable) {
                mView.onGroupListFetchFail();
                Log.e(TAG, "fetechGroupList - onFailure: " + "Lỗi truy vấn API");
            }
        });
    }
}
