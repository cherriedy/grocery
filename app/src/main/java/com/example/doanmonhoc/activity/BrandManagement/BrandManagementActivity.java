package com.example.doanmonhoc.activity.BrandManagement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
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
import com.example.doanmonhoc.contract.BrandManagement.ProductBrandManageContract;
import com.example.doanmonhoc.databinding.ActivityProductBrandBinding;
import com.example.doanmonhoc.model.Brand;
import com.example.doanmonhoc.presenter.BrandManagement.BrandManagePresenter;
import com.example.doanmonhoc.utils.IntentManager;

import java.util.List;

public class BrandManagementActivity extends AppCompatActivity implements ProductBrandManageContract.View, ProductBrandRecyclerViewAdapter.OnItemClickListener {
    private static final String TAG = "BrandManagementActivity";

    private ActivityProductBrandBinding binding;
    private Animation animFromBottomFab;
    private Animation animToBottomFab;
    private Animation animRotateClockWise;
    private Animation animRotateAntiClockWise;
    private boolean isSubMenuOpen;
    private ProductBrandRecyclerViewAdapter productBrandRecyclerViewAdapter;
    private BrandManagePresenter brandManagePresenter;
    private ActivityResultLauncher<Intent> getActivityResultOk;

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
        window.setStatusBarColor(ContextCompat.getColor(BrandManagementActivity.this, R.color.primaryColor));
        // Animation Handle
        initializeAnimation();
        // onClickListener Handle
        onExpandMenuClick();
        onAddOneClick();
        // Generate Objects
        brandManagePresenter = new BrandManagePresenter(this);
        productBrandRecyclerViewAdapter = new ProductBrandRecyclerViewAdapter(this);

        productBrandRecyclerViewAdapter.setOnItemClickListener(this);
        binding.listProductBrand.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        );
        brandManagePresenter.getProductBrandList();

        getActivityResultOk = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), o -> {
                    if (o.getResultCode() == Activity.RESULT_OK) {
                        productBrandRecyclerViewAdapter.setData(null);
                        binding.progressBar.setVisibility(View.VISIBLE);
                        brandManagePresenter.getProductBrandList();
                    }
                }
        );

        binding.buttonBack.setOnClickListener(v -> onBackPressed());
    }

    @Override
    public void getProductBrandListSuccessfully(List<Brand> brandList) {
        binding.progressBar.setVisibility(View.INVISIBLE);
        productBrandRecyclerViewAdapter.setData(brandList);
        binding.listProductBrand.setAdapter(productBrandRecyclerViewAdapter);
    }

    @Override
    public void getProductBrandListFail() {
        Toast.makeText(this, "Danh sách bị lỗi", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void itemClickListener(Brand brand) {
        Log.d(TAG, "itemClickListener: " + brand.toString());
        Intent intent = new Intent(this, AddOrEditBrandActivity.class);
        intent.putExtra(IntentManager.ModeParams.EXTRA_MODE, IntentManager.ModeParams.EXTRA_MODE_EDIT);
        intent.putExtra(IntentManager.ExtraParams.EXTRA_BRAND, brand);
        getActivityResultOk.launch(intent);
    }

    private void initializeAnimation() {
        animFromBottomFab = AnimationUtils.loadAnimation(BrandManagementActivity.this, R.anim.from_bottom_fab);
        animToBottomFab = AnimationUtils.loadAnimation(BrandManagementActivity.this, R.anim.to_bottom_fab);
        animRotateClockWise = AnimationUtils.loadAnimation(BrandManagementActivity.this, R.anim.rotate_clock_wise);
        animRotateAntiClockWise = AnimationUtils.loadAnimation(BrandManagementActivity.this, R.anim.rotate_anti_clock_wise);
    }

    private void onExpandMenuClick() {
        binding.fabExpandMenu.setOnClickListener(v -> {
            if (!isSubMenuOpen) {
                openMenu();
            } else {
                closeMenu();
            }
        });
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

    private void onAddOneClick() {
        binding.fabAddOne.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddOrEditBrandActivity.class);
            intent.putExtra(IntentManager.ModeParams.EXTRA_MODE, IntentManager.ModeParams.EXTRA_MODE_CREATE);
            getActivityResultOk.launch(intent);
        });
    }
}