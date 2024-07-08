package com.example.doanmonhoc.activity.Main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.doanmonhoc.R;
import com.example.doanmonhoc.activity.BrandManagement.BrandManagementActivity;
import com.example.doanmonhoc.activity.GroupManagement.GroupManagementActivity;
import com.example.doanmonhoc.activity.ImportManagement.ImportManagementActivity;
import com.example.doanmonhoc.activity.ProductManagement.ProductManagementActivity;
import com.example.doanmonhoc.activity.SaleManagement.SaleManagementActivity;
import com.example.doanmonhoc.adapter.ShortcutGridViewAdapter;
import com.example.doanmonhoc.databinding.FragmentStaffHomepageBinding;
import com.example.doanmonhoc.model.Shortcut;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StaffHomepageFragment extends Fragment implements ShortcutGridViewAdapter.OnItemClickListener {
    private FragmentStaffHomepageBinding binding;
    private List<Shortcut> mShortcutList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentStaffHomepageBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mShortcutList = Arrays.asList(
                new Shortcut("Sản phẩm", R.drawable.ic_product, R.color.primaryColor),
                new Shortcut("Đơn hàng", R.drawable.ic_note, R.color.primaryColor),
                new Shortcut("Thương hiệu", R.drawable.ic_product_brand, R.color.primaryColor),
                new Shortcut("Nhóm sản phẩm", R.drawable.ic_product_group, R.color.bold_sky)
        );

        ShortcutGridViewAdapter shortcutGridViewAdapter = new ShortcutGridViewAdapter(getContext());
        shortcutGridViewAdapter.setData(mShortcutList);
        shortcutGridViewAdapter.setOnItemClickListener(this);
        binding.gridViewShortcut.setAdapter(shortcutGridViewAdapter);
    }

    @Override
    public void onItemClick(int position) {
        @NonNull Map<Integer, Integer> shortcutMap = new HashMap<>();
        shortcutMap.put(0, ShortcutGridViewAdapter.SHORTCUT_PRODUCT);
        shortcutMap.put(1, ShortcutGridViewAdapter.SHORTCUT_INVENTORY);
        shortcutMap.put(2, ShortcutGridViewAdapter.SHORTCUT_BRAND);
        shortcutMap.put(3, ShortcutGridViewAdapter.SHORTCUT_TYPE);

        Integer shortcut = shortcutMap.get(position);
        if (shortcut != null) {
            if (shortcut == ShortcutGridViewAdapter.SHORTCUT_PRODUCT) {
                startActivity(new Intent(getContext(), ProductManagementActivity.class));
            } else if (shortcut == ShortcutGridViewAdapter.SHORTCUT_INVENTORY) {
                startActivity(new Intent(getContext(), SaleManagementActivity.class));
            } else if (shortcut == ShortcutGridViewAdapter.SHORTCUT_BRAND) {
                startActivity(new Intent(getContext(), BrandManagementActivity.class));
            } else if (shortcut == ShortcutGridViewAdapter.SHORTCUT_TYPE) {
                startActivity(new Intent(getContext(), GroupManagementActivity.class));
            }
        }
    }
}