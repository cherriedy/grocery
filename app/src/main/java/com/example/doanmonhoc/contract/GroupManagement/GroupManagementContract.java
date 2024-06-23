package com.example.doanmonhoc.contract.GroupManagement;

import com.example.doanmonhoc.model.ProductGroup;

import java.util.List;

public interface GroupManagementContract {
    interface View {
        void onGroupListFetchSuccess(List<ProductGroup> groupList);

        void onGroupListFetchFail();

        void createGroup();
    }

    interface Presenter {
        void fetchGroupList();
    }
}
