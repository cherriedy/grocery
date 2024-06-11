package com.example.doanmonhoc.activity.ProductBrandManagement;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
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

import java.util.List;

public class ProductBrandActivity extends AppCompatActivity implements
        ProductBrandManageContract.View,
        ProductBrandRecyclerViewAdapter.OnItemClickListener {

    private ActivityProductBrandBinding binding;
    private ProductBrandManagePresenter productBrandManagePresenter =
            new ProductBrandManagePresenter(ProductBrandActivity.this);

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
        window.setStatusBarColor(ContextCompat.getColor(ProductBrandActivity.this, R.color.primaryColor));

        binding.buttonBack.setOnClickListener(v -> onBackPressed());

        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(ProductBrandActivity.this, LinearLayoutManager.VERTICAL, false);
        binding.listProductBrand.setLayoutManager(linearLayoutManager);
        productBrandManagePresenter.getProductBrandList();
    }

    @Override
    public void getProductBrandListSuccessfully(List<Brand> brandList) {
        if (brandList != null) {
            ProductBrandRecyclerViewAdapter productBrandRecyclerViewAdapter =
                    new ProductBrandRecyclerViewAdapter(ProductBrandActivity.this);
            productBrandRecyclerViewAdapter.setData(brandList);
            productBrandRecyclerViewAdapter.setOnItemClickListener(ProductBrandActivity.this);
            binding.listProductBrand.setAdapter(productBrandRecyclerViewAdapter);
        }
    }

    @Override
    public void getProductBrandListFail() {
        Toast.makeText(this, "Danh sách bị lỗi", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void itemClickListener(Brand brand) {
        Toast.makeText(ProductBrandActivity.this, "WORKING", Toast.LENGTH_SHORT).show();
    }
}