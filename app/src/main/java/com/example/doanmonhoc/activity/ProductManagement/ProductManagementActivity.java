package com.example.doanmonhoc.activity.ProductManagement;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
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

public class ProductManagementActivity extends AppCompatActivity implements ProductManageContract {

    ActivityProductManagementBinding b;

    private boolean isSubMenuOpen;

    private Animation animFromBottomFab;
    private Animation animToBottomFab;
    private Animation animRotateClockWise;
    private Animation animRotateAntiClockWise;

    ProductManagePresenter productManagePresenter = new ProductManagePresenter(ProductManagementActivity.this);

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

        // Tạo LinearLayoutManager để quản lý các item
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ProductManagementActivity.this, RecyclerView.VERTICAL, false);
        // Thiết lập LayoutManager
        b.productList.setLayoutManager(linearLayoutManager);

        productManagePresenter.getProductList();

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
            startActivity(intent);
        });

        b.buttonTurnBack.setOnClickListener(v -> {
        });
    }

    @Override
    public void getProductListSuccessfully(List<Product> productList) {
        ProductRecyclerViewAdapter productRecyclerViewAdapter = new ProductRecyclerViewAdapter(ProductManagementActivity.this);
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