package com.example.doanmonhoc.activity.Main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.doanmonhoc.R;
import com.example.doanmonhoc.activity.ProductBrandManagement.ProductBrandActivity;
import com.example.doanmonhoc.activity.ProductManagement.ProductManagementActivity;
import com.example.doanmonhoc.activity.SaleManagement.SaleCreateActivity;
import com.example.doanmonhoc.activity.SaleManagement.SaleManagementActivity;
import com.example.doanmonhoc.adapter.ShortcutGridViewAdapter;
import com.example.doanmonhoc.databinding.FragmentHomepageBinding;

import java.util.Arrays;
import java.util.List;

public class HomepageFragment extends Fragment implements ShortcutGridViewAdapter.OnItemClickListener {
    private FragmentHomepageBinding b;
    private List<ShortcutGridViewAdapter.Shortcut> shortcutList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_homepage, container, false);
        b = FragmentHomepageBinding.inflate(getLayoutInflater());
        return b.getRoot();
    }

    @Override
   public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        shortcutList = Arrays.asList(
                new ShortcutGridViewAdapter.Shortcut("Sản phẩm", R.drawable.ic_product),
                new ShortcutGridViewAdapter.Shortcut("Thống kê", R.drawable.ic_chart),
                new ShortcutGridViewAdapter.Shortcut("Đơn hàng", R.drawable.ic_note),
                new ShortcutGridViewAdapter.Shortcut("Kiểm kho", R.drawable.ic_supplier),
                new ShortcutGridViewAdapter.Shortcut("Nhân viên", R.drawable.ic_customer),
                new ShortcutGridViewAdapter.Shortcut("Thêm", R.drawable.ic_circle_ellipsis)
        );

        ShortcutGridViewAdapter shortcutGridViewAdapter = new ShortcutGridViewAdapter(getContext());
        shortcutGridViewAdapter.setData(shortcutList);
        shortcutGridViewAdapter.setOnItemClickListener(this);
        b.gridViewShortcut.setAdapter(shortcutGridViewAdapter);
    }

    @Override
    public void onItemClick(int position) {
        if (position == ShortcutGridViewAdapter.SHORTCUT_PRODUCT) {
//            Toast.makeText(requireContext(), "PRODUCT", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getContext(), ProductManagementActivity.class));
        } else if (position == ShortcutGridViewAdapter.SHORTCUT_INVENTORY) {
            Toast.makeText(requireContext(), "INVENTORY", Toast.LENGTH_SHORT).show();
        } else if (position == ShortcutGridViewAdapter.SHORTCUT_ORDER) {
//            Toast.makeText(requireContext(), "ORDER", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getContext(), SaleManagementActivity.class));
        } else if (position == ShortcutGridViewAdapter.SHORTCUT_REPORT) {
            Toast.makeText(requireContext(), "REPORT", Toast.LENGTH_SHORT).show();
        } else if (position == ShortcutGridViewAdapter.SHORTCUT_STAFF) {
            Toast.makeText(requireContext(), "STAFF", Toast.LENGTH_SHORT).show();
        } else if (position == ShortcutGridViewAdapter.SHORTCUT_MORE) {
            Toast.makeText(requireContext(), "MORE", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getContext(), ProductBrandActivity.class));
        }
    }
}