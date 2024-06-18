package com.example.doanmonhoc.activity.SaleManagement;

import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.doanmonhoc.R;
import com.example.doanmonhoc.adapter.SaleCreateAdapter;
import com.example.doanmonhoc.adapter.SaleManagementAdapter;
import com.example.doanmonhoc.api.KiotApiService;
import com.example.doanmonhoc.model.CartItem;
import com.example.doanmonhoc.model.Product;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SaleCreateActivity extends AppCompatActivity {
    private GridView gridView;
    private SaleCreateAdapter adapter;
    private TextView tvTotalQuantity, tvTotalAmount;
    private CardView layoutBtnThanhToan, btnTiepTuc;
    private List<CartItem> cartItems ;
    private ImageButton btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sales_create);
        gridView = findViewById(R.id.gridView);
        layoutBtnThanhToan = findViewById(R.id.layoutBtnThanhToan);
        tvTotalQuantity = findViewById(R.id.tvTotalQuantityInCart);
        tvTotalAmount = findViewById(R.id.tvTotalPriceInCart);


        loadProductList();

        cartItems = new ArrayList<>();

        btnTiepTuc = findViewById(R.id.btnTiepTuc);
        btnTiepTuc.setOnClickListener(v -> {
            if (cartItems != null) {
                Intent intent = new Intent(this, SaleConfirmActivity.class);
                intent.putExtra("cartItems", new ArrayList<>(cartItems));
                startActivity(intent);
            } else {
                Toast.makeText(this, "Giỏ hàng rỗng!", Toast.LENGTH_SHORT).show();
            }
        });

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());


    }

    private void loadProductList() {
        KiotApiService.apiService.getProductList().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    List<Product> list = response.body();
                    if (list != null && !list.isEmpty()) {
                        adapter = new SaleCreateAdapter(SaleCreateActivity.this, list, layoutBtnThanhToan, tvTotalQuantity, tvTotalAmount, cartItems);
                        gridView.setAdapter(adapter);
                    } else {
                        Toast.makeText(SaleCreateActivity.this, "Không tìm thấy hóa đơn nào!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SaleCreateActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(SaleCreateActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}