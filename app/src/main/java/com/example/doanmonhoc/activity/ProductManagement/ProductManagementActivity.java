package com.example.doanmonhoc.activity.ProductManagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanmonhoc.R;
import com.example.doanmonhoc.adapter.ProductListAdapter;
import com.example.doanmonhoc.databinding.ActivityProductManagementBinding;
import com.example.doanmonhoc.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductManagementActivity extends AppCompatActivity {

    ActivityProductManagementBinding binding;

    // This check if all sub fabs are visible or not
    private boolean isSubMenuOpen;

    private Animation animFromBottomFab;
    private Animation animToBottomFab;
    private Animation animRotateClockWise;
    private Animation animRotateAntiClockWise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // Thiết lập ViewBinding
        binding = ActivityProductManagementBinding.inflate(getLayoutInflater());
        // Gán layout cho activity
        setContentView(binding.getRoot());

//        setContentView(R.layout.activity_product_management);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Tạo Adpater cho productList (RecyclerView)
        ProductListAdapter productListAdapter = new ProductListAdapter(ProductManagementActivity.this);
        // Tạo LinearLayoutManager để quản lý các item
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ProductManagementActivity.this, RecyclerView.VERTICAL, false);
        // Thiết lập LayoutManager
        binding.productList.setLayoutManager(linearLayoutManager);

        // Gán mảng dữ liệu cho Adapter thông qua gọi phương thức getListData
        productListAdapter.setData(getListData());
        // Gán Adapter cho productView
        binding.productList.setAdapter(productListAdapter);

        initializeAnimation();

        binding.addButton.setOnClickListener(v -> {
            if (!isSubMenuOpen) {
                openMenu();
            } else {
                closeMenu();
            }
        });

        binding.addOne.setOnClickListener(v -> {
            Intent intent = new Intent(ProductManagementActivity.this, AddProductActivity.class);
            startActivity(intent);
        });
    }

    private void initializeAnimation() {
        animFromBottomFab = AnimationUtils.loadAnimation(ProductManagementActivity.this, R.anim.from_bottom_fab);
        animToBottomFab = AnimationUtils.loadAnimation(ProductManagementActivity.this, R.anim.to_bottom_fab);
        animRotateClockWise = AnimationUtils.loadAnimation(ProductManagementActivity.this, R.anim.rotate_clock_wise);
        animRotateAntiClockWise = AnimationUtils.loadAnimation(ProductManagementActivity.this, R.anim.rotate_anti_clock_wise);
    }

    private void closeMenu() {
        binding.addButton.startAnimation(animRotateAntiClockWise);
        binding.addOne.startAnimation(animToBottomFab);
        binding.addMany.startAnimation(animToBottomFab);
        binding.addOneTxt.startAnimation(animToBottomFab);
        binding.addManyTxt.startAnimation(animToBottomFab);
        isSubMenuOpen = false;
    }

    private void openMenu() {
        binding.addButton.startAnimation(animRotateClockWise);
        binding.addOne.startAnimation(animFromBottomFab);
        binding.addMany.startAnimation(animFromBottomFab);
        binding.addOneTxt.startAnimation(animFromBottomFab);
        binding.addManyTxt.startAnimation(animFromBottomFab);
        isSubMenuOpen = true;
    }

    private List<Product> getListData() {
        List<Product> list = new ArrayList<>();

        list.add(new Product("maggi", 100.3f, R.drawable.maggi));
        list.add(new Product("burger", 100.3f, R.drawable.burge));
        list.add(new Product("cake", 100.3f, R.drawable.cake));
        list.add(new Product("fries", 100.3f, R.drawable.fries));
        list.add(new Product("pancake", 100.3f, R.drawable.pancake));
        list.add(new Product("pasta", 100.3f, R.drawable.pasta));
        list.add(new Product("pizza", 100.3f, R.drawable.pizza));

        return list;
    }
}