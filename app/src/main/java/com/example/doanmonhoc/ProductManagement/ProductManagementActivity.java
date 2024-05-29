package com.example.doanmonhoc.ProductManagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanmonhoc.Adapter.ProductListAdapter;
import com.example.doanmonhoc.Model.Product;
import com.example.doanmonhoc.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ProductManagementActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private FloatingActionButton fabAdd;
    private FloatingActionButton fabAddOne;
    private FloatingActionButton fabAddMany;
    private TextView txtAddOne;
    private TextView txtAddMany;

    // This check if all sub fabs are visible or not
    private boolean isSubMenuOpen;

    private Animation animFromBottomFab;
    private Animation animToBottomFab;
    private Animation animRotateClockWise;
    private Animation animRotateAntiCloseWise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_management);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.product_list);
        ProductListAdapter productListAdapter = new ProductListAdapter(ProductManagementActivity.this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ProductManagementActivity.this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        productListAdapter.setData(getListData());
        recyclerView.setAdapter(productListAdapter);

        initializeView();
        initializeAnimation();

        fabAdd.setOnClickListener(v -> {
            if (!isSubMenuOpen) {
                openMenu();
            } else {
                closeMenu();
            }
        });

        fabAddOne.setOnClickListener(v -> {
            Intent intent = new Intent(ProductManagementActivity.this, AddProductActivity.class);
            startActivity(intent);
        });
    }

    private void initializeView() {
        fabAdd = findViewById(R.id.add_button);
        fabAddOne = findViewById(R.id.add_one);
        fabAddMany = findViewById(R.id.add_many);
        txtAddOne = findViewById(R.id.add_one_txt);
        txtAddMany = findViewById(R.id.add_many_txt);
    }

    private void initializeAnimation() {
        animFromBottomFab = AnimationUtils.loadAnimation(ProductManagementActivity.this, R.anim.from_bottom_fab);
        animToBottomFab = AnimationUtils.loadAnimation(ProductManagementActivity.this, R.anim.to_bottom_fab);
        animRotateClockWise = AnimationUtils.loadAnimation(ProductManagementActivity.this, R.anim.rotate_clock_wise);
        animRotateAntiCloseWise = AnimationUtils.loadAnimation(ProductManagementActivity.this, R.anim.rotate_anti_clock_wise);
    }

    private void closeMenu() {
        fabAdd.startAnimation(animRotateAntiCloseWise);
        fabAddOne.startAnimation(animToBottomFab);
        fabAddMany.startAnimation(animToBottomFab);
        txtAddOne.startAnimation(animToBottomFab);
        txtAddMany.startAnimation(animToBottomFab);
        isSubMenuOpen = false;
    }

    private void openMenu() {
        fabAdd.startAnimation(animRotateClockWise);
        fabAddOne.startAnimation(animFromBottomFab);
        fabAddMany.startAnimation(animFromBottomFab);
        txtAddOne.startAnimation(animFromBottomFab);
        txtAddMany.startAnimation(animFromBottomFab);
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