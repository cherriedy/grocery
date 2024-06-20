package com.example.doanmonhoc.presenter.GroupManagement;

import android.content.Intent;

import com.example.doanmonhoc.contract.GroupManagement.AddOrEditGroupContract;
import com.example.doanmonhoc.model.ProductGroup;
import com.example.doanmonhoc.utils.IntentManager;

public class AddOrEditGroupPresenter implements AddOrEditGroupContract.Presenter {
    private static final String TAG = AddOrEditGroupPresenter.class.getSimpleName();

    private final AddOrEditGroupContract.View mView;

    public AddOrEditGroupPresenter(AddOrEditGroupContract.View view) {
        mView = view;
    }

    @Override
    public void handleExtraGroup(Intent intent) {
        ProductGroup group = (ProductGroup) intent.getSerializableExtra(IntentManager.ExtraParams.EXTRA_GROUP);
        if (group == null) {
            mView.onExtraGroupProcessingFail();
            return;
        }

        mView.onExtraGroupProcessingSuccess(group);
    }

    @Override
    public void handleCreateGroup(ProductGroup group) {
    }

    @Override
    public void handleUpdateGroup(ProductGroup group) {

    }

    @Override
    public void handleDeleteGroup(long id) {

    }
}
