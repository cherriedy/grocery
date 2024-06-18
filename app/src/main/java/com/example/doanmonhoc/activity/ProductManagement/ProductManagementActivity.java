package com.example.doanmonhoc.activity.ProductManagement;

import android.app.Activity;
import android.content.Intent;
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

import java.util.List;

public class ProductManagementActivity extends AppCompatActivity implements ProductManageContract.View, ProductRecyclerViewAdapter.OnItemClickListener {
    public static final String EXTRA_PRODUCT = "EXTRA_PRODUCT";

    private ActivityProductManagementBinding binding;
    private boolean isSubMenuOpen;
    private Animation animFromBottomFab;
    private Animation animToBottomFab;
    private Animation animRotateClockWise;
    private Animation animRotateAntiClockWise;
    private ProductManagePresenter productManagePresenter;
    private ProductRecyclerViewAdapter productRecyclerViewAdapter;
    private ActivityResultLauncher<Intent> getActivityResultOk;

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
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));

        // Thiết lập animation cho fab
        initializeAnimation();

        getActivityResultOk = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), o -> {
                    if (o.getResultCode() == Activity.RESULT_OK) {
                        productRecyclerViewAdapter.setData(null);
                        binding.progressBar.setVisibility(View.VISIBLE);
                        productManagePresenter.getProductList();
                    }
                }
        );

        productManagePresenter = new ProductManagePresenter(this);
        productRecyclerViewAdapter = new ProductRecyclerViewAdapter(this, this);

        // Thiết lập LayoutManager
        binding.productList.setLayoutManager(
                new LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        );

        // Gọi callback tới presenter, lấy dữ liệu từ api
        productManagePresenter.getProductList();

        onExpandMenuClick();
        onAddOneClick();
        binding.buttonBack.setOnClickListener(v -> onBackPressed());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void getProductListSuccessfully(List<Product> productList) {
        binding.progressBar.setVisibility(View.INVISIBLE);
        productRecyclerViewAdapter.setData(productList);
        binding.productList.setAdapter(productRecyclerViewAdapter);
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
        animFromBottomFab = AnimationUtils.loadAnimation(ProductManagementActivity.this, R.anim.from_bottom_fab);
        animToBottomFab = AnimationUtils.loadAnimation(ProductManagementActivity.this, R.anim.to_bottom_fab);
        animRotateClockWise = AnimationUtils.loadAnimation(ProductManagementActivity.this, R.anim.rotate_clock_wise);
        animRotateAntiClockWise = AnimationUtils.loadAnimation(ProductManagementActivity.this, R.anim.rotate_anti_clock_wise);
    }

    private void closeMenu() {
        binding.fabExpandMenu.startAnimation(animRotateAntiClockWise);
        binding.fabAddOne.startAnimation(animToBottomFab);
        binding.addOneTxt.startAnimation(animToBottomFab);
        isSubMenuOpen = false;
    }

    private void openMenu() {
        binding.fabExpandMenu.startAnimation(animRotateClockWise);
        binding.fabAddOne.startAnimation(animFromBottomFab);
        binding.addOneTxt.startAnimation(animFromBottomFab);
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
}