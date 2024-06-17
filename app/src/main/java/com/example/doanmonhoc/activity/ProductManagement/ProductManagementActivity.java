package com.example.doanmonhoc.activity.ProductManagement;

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
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanmonhoc.R;
import com.example.doanmonhoc.adapter.ProductRecyclerViewAdapter;
import com.example.doanmonhoc.contract.ProductManagement.ProductManageContract;
import com.example.doanmonhoc.databinding.ActivityProductManagementBinding;
import com.example.doanmonhoc.model.Product;
import com.example.doanmonhoc.presenter.ProductManagament.ProductManagePresenter;
import com.example.doanmonhoc.utils.ExtraManager;

import java.util.List;

public class ProductManagementActivity extends AppCompatActivity implements ProductManageContract.View,
        ProductRecyclerViewAdapter.OnItemClickListener {

    public static final String EXTRA_PRODUCT = "EXTRA_PRODUCT";

    private ActivityProductManagementBinding binding;
    private boolean isSubMenuOpen;
    private Animation animFromBottomFab;
    private Animation animToBottomFab;
    private Animation animRotateClockWise;
    private Animation animRotateAntiClockWise;
    private ProductManagePresenter productManagePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityProductManagementBinding.inflate(getLayoutInflater());              // Thiết lập ViewBinding
        setContentView(binding.getRoot());                                                    // Gán layout cho activity
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Xét màu cho status bar
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));

        productManagePresenter = new ProductManagePresenter(ProductManagementActivity.this);

        // Tạo LinearLayoutManager để quản lý các item
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(ProductManagementActivity.this, RecyclerView.VERTICAL, false);
        // Thiết lập LayoutManager
        binding.productList.setLayoutManager(linearLayoutManager);
        //
        binding.productList.setNestedScrollingEnabled(true);
        // Gọi callback tới presenter, lấy dữ liệu từ api
        productManagePresenter.getProductList();

        initializeAnimation();

        binding.addButton.setOnClickListener(v -> {
            if (!isSubMenuOpen) {
                openMenu();
            } else {
                closeMenu();
            }
        });

        ActivityResultLauncher<Intent> addProductResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult o) {
                        if (o.getResultCode() == Activity.RESULT_OK) {
                            productManagePresenter.getProductList();
                        }
                    }
                }
        );

        binding.addOne.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddProductActivity.class);
            intent.putExtra(ExtraManager.ModeParams.EXTRA_MODE, ExtraManager.ModeParams.EXTRA_MODE_CREATE);
            addProductResultLauncher.launch(intent);
        });

        binding.buttonBack.setOnClickListener(v -> onBackPressed());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void getProductListSuccessfully(List<Product> productList) {
        binding.progressBar.setVisibility(View.INVISIBLE);
        ProductRecyclerViewAdapter productRecyclerViewAdapter =
                new ProductRecyclerViewAdapter(this, this);
        productRecyclerViewAdapter.setData(productList);
        binding.productList.setAdapter(productRecyclerViewAdapter);
    }

    @Override
    public void getProductListFail(Throwable throwable) {
        Log.i("API", throwable.getMessage());
        Toast.makeText(ProductManagementActivity.this, "Lỗi!", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onItemClick(Product product) {
        Intent intent = new Intent(this, AddProductActivity.class);
        intent.putExtra(ExtraManager.ModeParams.EXTRA_MODE, ExtraManager.ModeParams.EXTRA_MODE_EDIT);
        intent.putExtra(EXTRA_PRODUCT, product);
        startActivity(intent);
    }
}