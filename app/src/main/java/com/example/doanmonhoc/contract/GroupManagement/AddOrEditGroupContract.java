package com.example.doanmonhoc.contract.GroupManagement;

import android.content.Intent;

import com.example.doanmonhoc.model.ProductGroup;

public interface AddOrEditGroupContract {
    interface View {
        void onExtraGroupProcessingSuccess(ProductGroup group);

        void onExtraGroupProcessingFail();

        void createGroup();

        void onGroupCreationSuccess();

        void onGroupCreationFail();

        void updateGroup();

        void onGroupUpdateSuccess();

        void onGroupUpdateFail();

        void deleteGroup();

        void onGroupDeleteSuccess();

        void onGroupDeleteFail();
    }

    interface Presenter {
        void handleExtraGroup(Intent intent);

        void handleCreateGroup(ProductGroup group);

        void handleUpdateGroup(ProductGroup group);

        void handleDeleteGroup(long id);
    }
}
