package com.example.doanmonhoc.activity.ProductManagement;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanmonhoc.R;
import com.example.doanmonhoc.adapter.ProductRecyclerViewAdapter;
import com.example.doanmonhoc.contract.ProductManagement.ProductManageContract;
import com.example.doanmonhoc.databinding.ActivityProductManagementBinding;
import com.example.doanmonhoc.model.Product;
import com.example.doanmonhoc.presenter.ProductManagament.ProductManagePresenter;
import com.example.doanmonhoc.utils.IntentManager;
import com.example.doanmonhoc.utils.PrefsUtils;

import java.util.List;

public class ProductManagementActivity extends AppCompatActivity implements ProductManageContract.View,
        ProductRecyclerViewAdapter.OnItemClickListener {
    public static final String EXTRA_PRODUCT = "EXTRA_PRODUCT";

    private ActivityProductManagementBinding binding;
    private boolean isSubMenuOpen;
    private Animation mAnimFromBottomFab;
    private Animation mAnimToBottomFab;
    private Animation mAnimRotateClockWise;
    private Animation mAnimRotateAntiClockWise;
    private ProductManagePresenter mPresenter;
    private ProductRecyclerViewAdapter mProductAdapter;
    private ActivityResultLauncher<Intent> getActivityResultOk;
    private SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityProductManagementBinding.inflate(getLayoutInflater());
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
        binding.actionBack.setOnClickListener(v -> onBackPressed());

        getActivityResultOk = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), o -> {
                    if (o.getResultCode() == Activity.RESULT_OK) {
                        mProductAdapter.setData(null);
                        binding.progressBar.setVisibility(View.VISIBLE);
                        mPresenter.getProductList();
                    }
                }
        );

        mPresenter = new ProductManagePresenter(this);
        mProductAdapter = new ProductRecyclerViewAdapter(this, this);
        mPrefs = getSharedPreferences("myPrefs", MODE_PRIVATE);

        // Thiết lập LayoutManager
        binding.listProduct.setLayoutManager(
                new LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        );
        binding.listProduct.getRecycledViewPool().setMaxRecycledViews(0, 0);

        handleFeatureByRole();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Gọi callback tới presenter, lấy dữ liệu từ api
        mPresenter.getProductList();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void getProductListSuccessfully(List<Product> productList) {
        binding.progressBar.setVisibility(View.INVISIBLE);
        mProductAdapter.setData(productList);
        binding.listProduct.setAdapter(mProductAdapter);
    }

    @Override
    public void getProductListFail() {
        binding.progressBar.setVisibility(View.INVISIBLE);
        Toast.makeText(ProductManagementActivity.this, "Lỗi!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(Product product) {
        Intent intent = new Intent(this, AddOrEditProductActivity.class);
        intent.putExtra(IntentManager.ModeParams.EXTRA_MODE, IntentManager.ModeParams.EXTRA_MODE_EDIT);
        intent.putExtra(EXTRA_PRODUCT, product);
        getActivityResultOk.launch(intent);
    }

    private void initializeAnimation() {
        mAnimFromBottomFab = AnimationUtils.loadAnimation(this, R.anim.from_bottom_fab);
        mAnimToBottomFab = AnimationUtils.loadAnimation(this, R.anim.to_bottom_fab);
        mAnimRotateClockWise = AnimationUtils.loadAnimation(this, R.anim.rotate_clock_wise);
        mAnimRotateAntiClockWise = AnimationUtils.loadAnimation(this, R.anim.rotate_anti_clock_wise);
    }

    private void closeMenu() {
        binding.fabExpandMenu.startAnimation(mAnimRotateAntiClockWise);
        binding.fabAddOne.startAnimation(mAnimToBottomFab);
        binding.textFabAddOne.startAnimation(mAnimToBottomFab);
        isSubMenuOpen = false;
    }

    private void openMenu() {
        binding.fabExpandMenu.startAnimation(mAnimRotateClockWise);
        binding.fabAddOne.startAnimation(mAnimFromBottomFab);
        binding.textFabAddOne.startAnimation(mAnimFromBottomFab);
        isSubMenuOpen = true;
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

    private void onAddOneClick() {
        binding.fabAddOne.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddOrEditProductActivity.class);
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