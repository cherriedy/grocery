package com.example.doanmonhoc.activity.ProductManagement;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
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
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanmonhoc.R;
import com.example.doanmonhoc.adapter.ProductRecyclerViewAdapter;
import com.example.doanmonhoc.contract.ProductManageContract;
import com.example.doanmonhoc.databinding.ActivityProductManagementBinding;
import com.example.doanmonhoc.model.Product;
import com.example.doanmonhoc.presenter.ProductManagePresenter;

import java.util.List;

public class ProductManagementActivity extends AppCompatActivity implements ProductManageContract.View {

    public static final String EXTRA_PRODUCT = "EXTRA_PRODUCT";
    public static final String EXTRA_MODE_CREATE = "EXTRA_NODE_CREATE";
    public static final String EXTRA_MODE_UPDATE = "EXTRA_MODE_UPDATE";
    private ActivityProductManagementBinding b;
    private boolean isSubMenuOpen;
    private Animation animFromBottomFab;
    private Animation animToBottomFab;
    private Animation animRotateClockWise;
    private Animation animRotateAntiClockWise;
    private ProductManagePresenter productManagePresenter;

    ProductRecyclerViewAdapter.OnItemClickListener onItemClickListener = new ProductRecyclerViewAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(Product product) {
            Intent intent = new Intent(ProductManagementActivity.this, AddProductActivity.class);
            intent.putExtra("MODE", EXTRA_MODE_UPDATE);
            intent.putExtra(EXTRA_PRODUCT, product);
            startActivity(intent);
        }
    };

//    ActivityResultLauncher<Intent> startProductDetailActivityIntent = registerForActivityResult(
//            new ActivityResultContracts.StartActivityForResult(),
//            new ActivityResultCallback<ActivityResult>() {
//                @Override
//                public void onActivityResult(ActivityResult o) {
//                    if (o.getResultCode() == RESULT_OK) {
//
//                    }
//                }
//            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        b = ActivityProductManagementBinding.inflate(getLayoutInflater());              // Thiết lập ViewBinding
        setContentView(b.getRoot());                                                    // Gán layout cho activity

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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ProductManagementActivity.this, RecyclerView.VERTICAL, false);
        b.productList.setLayoutManager(linearLayoutManager);                   // Thiết lập LayoutManager
        productManagePresenter.getProductList();                               // Gọi callback tới presenter, lấy dữ liệu từ api

        initializeAnimation();

        b.addButton.setOnClickListener(v -> {
            if (!isSubMenuOpen) {
                openMenu();
            } else {
                closeMenu();
            }
        });

        b.addOne.setOnClickListener(v -> {
            Intent intent = new Intent(ProductManagementActivity.this, AddProductActivity.class);
            intent.putExtra("MODE", EXTRA_MODE_CREATE);
            startActivity(intent);
        });

        b.buttonBack.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void getProductListSuccessfully(List<Product> productList) {
        ProductRecyclerViewAdapter productRecyclerViewAdapter = new ProductRecyclerViewAdapter(ProductManagementActivity.this, onItemClickListener);
        productRecyclerViewAdapter.setData(productList);
        b.productList.setAdapter(productRecyclerViewAdapter);
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
        b.addButton.startAnimation(animRotateAntiClockWise);
        b.addOne.startAnimation(animToBottomFab);
        b.addMany.startAnimation(animToBottomFab);
        b.addOneTxt.startAnimation(animToBottomFab);
        b.addManyTxt.startAnimation(animToBottomFab);
        isSubMenuOpen = false;
    }

    private void openMenu() {
        b.addButton.startAnimation(animRotateClockWise);
        b.addOne.startAnimation(animFromBottomFab);
        b.addMany.startAnimation(animFromBottomFab);
        b.addOneTxt.startAnimation(animFromBottomFab);
        b.addManyTxt.startAnimation(animFromBottomFab);
        isSubMenuOpen = true;
    }
}