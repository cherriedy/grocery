package com.example.doanmonhoc.activity.product;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanmonhoc.R;
import com.example.doanmonhoc.adapter.ProductRecyclerViewAdapter;
import com.example.doanmonhoc.databinding.ActivityProductManagementBinding;
import com.example.doanmonhoc.model.Product;
import com.example.doanmonhoc.presenter.ProductManagePresenter;
import com.example.doanmonhoc.contract.ProductManageContract;

import java.util.ArrayList;
import java.util.List;

public class ProductManageActivity extends AppCompatActivity implements ProductManageContract {

    ActivityProductManagementBinding binding;

    private List<Product> productList;

    private boolean isSubMenuOpen;

    private Animation animFromBottomFab;
    private Animation animToBottomFab;
    private Animation animRotateClockWise;
    private Animation animRotateAntiClockWise;

    ProductManagePresenter productManagePresenter = new ProductManagePresenter(ProductManageActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // Thiết lập ViewBinding
        binding = ActivityProductManagementBinding.inflate(getLayoutInflater());
        // Gán layout cho activity
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Tạo LinearLayoutManager để quản lý các item
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ProductManageActivity.this, RecyclerView.VERTICAL, false);
        // Thiết lập LayoutManager
        binding.productList.setLayoutManager(linearLayoutManager);

        productList = new ArrayList<>();
//        callApiGetProductList();
        productManagePresenter.getProductList();

        initializeAnimation();

        binding.addButton.setOnClickListener(v -> {
            if (!isSubMenuOpen) {
                openMenu();
            } else {
                closeMenu();
            }
        });

        binding.addOne.setOnClickListener(v -> {
            Intent intent = new Intent(ProductManageActivity.this, ProductAddActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void getProductListSuccessfully(List<Product> productList) {
        ProductRecyclerViewAdapter productRecyclerViewAdapter = new ProductRecyclerViewAdapter(ProductManageActivity.this);
        productRecyclerViewAdapter.setData(productList);
        binding.productList.setAdapter(productRecyclerViewAdapter);
    }

    @Override
    public void getProductListFail(Throwable throwable) {
        Log.i("API", throwable.getMessage());
        Toast.makeText(ProductManageActivity.this, "Lỗi!", Toast.LENGTH_SHORT).show();
    }

    private void initializeAnimation() {
        animFromBottomFab = AnimationUtils.loadAnimation(ProductManageActivity.this, R.anim.from_bottom_fab);
        animToBottomFab = AnimationUtils.loadAnimation(ProductManageActivity.this, R.anim.to_bottom_fab);
        animRotateClockWise = AnimationUtils.loadAnimation(ProductManageActivity.this, R.anim.rotate_clock_wise);
        animRotateAntiClockWise = AnimationUtils.loadAnimation(ProductManageActivity.this, R.anim.rotate_anti_clock_wise);
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


//    private List<Product1> getListData() {
//        List<Product1> list = new ArrayList<>();
//
//        list.add(new Product1("maggi", 100.3f, R.drawable.maggi));
//        list.add(new Product1("burger", 100.3f, R.drawable.burge));
//        list.add(new Product1("cake", 100.3f, R.drawable.cake));
//        list.add(new Product1("fries", 100.3f, R.drawable.fries));
//        list.add(new Product1("pancake", 100.3f, R.drawable.pancake));
//        list.add(new Product1("pasta", 100.3f, R.drawable.pasta));
//        list.add(new Product1("pizza", 100.3f, R.drawable.pizza));
//
//        return list;
//    }
}