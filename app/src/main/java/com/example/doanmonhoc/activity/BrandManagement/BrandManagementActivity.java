package com.example.doanmonhoc.activity.BrandManagement;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.doanmonhoc.utils.PrefsUtils;

import java.util.List;

public class BrandManagementActivity extends AppCompatActivity implements ProductBrandManageContract.View, ProductBrandRecyclerViewAdapter.OnItemClickListener {
    private static final String TAG = "BrandManagementActivity";

    private ActivityProductBrandBinding binding;
    private Animation mAnimFromBottomFab;
    private Animation mAnimToBottomFab;
    private Animation mAnimRotateClockWise;
    private Animation mAnimRotateAntiClockWise;
    private boolean isSubMenuOpen;
    private ProductBrandRecyclerViewAdapter mBrandAdapter;
    private BrandManagePresenter mPresenter;
    private ActivityResultLauncher<Intent> getActivityResultOk;
    private SharedPreferences mPrefs;

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
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.primaryColor));

        initializeAnimation();
        onExpandMenuClick();
        onAddOneClick();

        mPresenter = new BrandManagePresenter(this);
        mBrandAdapter = new ProductBrandRecyclerViewAdapter(this);
        mPrefs = getSharedPreferences("myPrefs", MODE_PRIVATE);

        mBrandAdapter.setOnItemClickListener(this);
        binding.listProductBrand.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        getActivityResultOk = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), o -> {
                    if (o.getResultCode() == Activity.RESULT_OK) {
                        mBrandAdapter.setData(null);
                        binding.progressBar.setVisibility(View.VISIBLE);
                        mPresenter.getProductBrandList();
                    }
                }
        );

        binding.actionBack.setOnClickListener(v -> onBackPressed());
        handleFeatureByRole();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.getProductBrandList();
    }

    @Override
    public void getProductBrandListSuccessfully(List<Brand> brandList) {
        binding.progressBar.setVisibility(View.INVISIBLE);
        mBrandAdapter.setData(brandList);
        binding.listProductBrand.setAdapter(mBrandAdapter);
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
        mAnimFromBottomFab = AnimationUtils.loadAnimation(this, R.anim.from_bottom_fab);
        mAnimToBottomFab = AnimationUtils.loadAnimation(this, R.anim.to_bottom_fab);
        mAnimRotateClockWise = AnimationUtils.loadAnimation(this, R.anim.rotate_clock_wise);
        mAnimRotateAntiClockWise = AnimationUtils.loadAnimation(this, R.anim.rotate_anti_clock_wise);
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
        binding.fabExpandMenu.startAnimation(mAnimRotateClockWise);
        binding.fabAddOne.startAnimation(mAnimFromBottomFab);
        binding.textFabAddOne.startAnimation(mAnimFromBottomFab);
        isSubMenuOpen = true;
    }

    private void closeMenu() {
        binding.fabExpandMenu.startAnimation(mAnimRotateAntiClockWise);
        binding.fabAddOne.startAnimation(mAnimToBottomFab);
        binding.textFabAddOne.startAnimation(mAnimToBottomFab);
        isSubMenuOpen = false;
    }

    private void onAddOneClick() {
        binding.fabAddOne.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddOrEditBrandActivity.class);
            intent.putExtra(IntentManager.ModeParams.EXTRA_MODE, IntentManager.ModeParams.EXTRA_MODE_CREATE);
            getActivityResultOk.launch(intent);
        });
    }

    private void handleFeatureByRole() {
        if (PrefsUtils.getRoldId(mPrefs) == 2) {
            binding.fabExpandMenu.setVisibility(View.GONE);
        }
    }
}