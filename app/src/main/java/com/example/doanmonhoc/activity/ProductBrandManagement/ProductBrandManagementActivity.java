package com.example.doanmonhoc.activity.ProductBrandManagement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.doanmonhoc.R;
import com.example.doanmonhoc.adapter.ProductBrandRecyclerViewAdapter;
import com.example.doanmonhoc.contract.ProductBrandManagement.ProductBrandManageContract;
import com.example.doanmonhoc.databinding.ActivityProductBrandBinding;
import com.example.doanmonhoc.model.Brand;
import com.example.doanmonhoc.presenter.ProductBrandManagement.ProductBrandManagePresenter;
import com.example.doanmonhoc.utils.ExtraManager;

import java.util.List;

public class ProductBrandManagementActivity extends AppCompatActivity implements ProductBrandManageContract.View, ProductBrandRecyclerViewAdapter.OnItemClickListener {

    private Animation animFromBottomFab;
    private Animation animToBottomFab;
    private Animation animRotateClockWise;
    private Animation animRotateAntiClockWise;
    private boolean isSubMenuOpen;
    private ProductBrandRecyclerViewAdapter productBrandRecyclerViewAdapter;
    private ActivityProductBrandBinding binding;
    private ProductBrandManagePresenter productBrandManagePresenter = new ProductBrandManagePresenter(ProductBrandManagementActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityProductBrandBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(ProductBrandManagementActivity.this, R.color.primaryColor));

        productBrandRecyclerViewAdapter = new ProductBrandRecyclerViewAdapter(ProductBrandManagementActivity.this);

        initializeAnimation();

        binding.fabExpandMenu.setOnClickListener(v -> {
            if (!isSubMenuOpen) {
                openMenu();
            } else {
                closeMenu();
            }
        });

        ActivityResultLauncher<Intent> addProductBrandResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult o) {
                        if (o.getResultCode() == Activity.RESULT_OK) {
                            productBrandManagePresenter.getProductBrandList();
                        }
                    }
                }
        );

        binding.fabAddOne.setOnClickListener(v -> {
            Intent intent = new Intent(ProductBrandManagementActivity.this, AddProductBrandActivity.class);
            intent.putExtra(ExtraManager.ModeParams.EXTRA_MODE, ExtraManager.ModeParams.EXTRA_MODE_CREATE);
            addProductBrandResultLauncher.launch(intent);
        });

        binding.buttonBack.setOnClickListener(v -> onBackPressed());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ProductBrandManagementActivity.this, LinearLayoutManager.VERTICAL, false);
        binding.listProductBrand.setLayoutManager(linearLayoutManager);
        productBrandManagePresenter.getProductBrandList();
    }


    @Override
    public void getProductBrandListSuccessfully(List<Brand> brandList) {
        if (brandList != null) {
            productBrandRecyclerViewAdapter.setData(brandList);
            productBrandRecyclerViewAdapter.setOnItemClickListener(ProductBrandManagementActivity.this);
            binding.listProductBrand.setAdapter(productBrandRecyclerViewAdapter);
        }
    }

    @Override
    public void getProductBrandListFail() {
        Toast.makeText(this, "Danh sách bị lỗi", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void itemClickListener(Brand brand) {
        Toast.makeText(ProductBrandManagementActivity.this, "WORKING", Toast.LENGTH_SHORT).show();
    }

    private void openMenu() {
        binding.fabExpandMenu.startAnimation(animRotateClockWise);
        binding.fabAddOne.startAnimation(animFromBottomFab);
        binding.textAddOne.startAnimation(animFromBottomFab);
        isSubMenuOpen = true;
    }

    private void closeMenu() {
        binding.fabExpandMenu.startAnimation(animRotateAntiClockWise);
        binding.fabAddOne.startAnimation(animToBottomFab);
        binding.textAddOne.startAnimation(animToBottomFab);
        isSubMenuOpen = false;
    }

    private void initializeAnimation() {
        animFromBottomFab = AnimationUtils.loadAnimation(ProductBrandManagementActivity.this, R.anim.from_bottom_fab);
        animToBottomFab = AnimationUtils.loadAnimation(ProductBrandManagementActivity.this, R.anim.to_bottom_fab);
        animRotateClockWise = AnimationUtils.loadAnimation(ProductBrandManagementActivity.this, R.anim.rotate_clock_wise);
        animRotateAntiClockWise = AnimationUtils.loadAnimation(ProductBrandManagementActivity.this, R.anim.rotate_anti_clock_wise);
    }

}