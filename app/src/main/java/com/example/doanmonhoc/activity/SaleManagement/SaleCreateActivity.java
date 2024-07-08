package com.example.doanmonhoc.activity.SaleManagement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.doanmonhoc.R;
import com.example.doanmonhoc.adapter.SaleCreateAdapter;
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
    private EditText etSearch;
    private List<Product> productList = new ArrayList<>();
    private List<Product> filteredProductList = new ArrayList<>();

    // Khởi tạo ActivityResultLauncher
    private final ActivityResultLauncher<Intent> activityResultFinish = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    finish();
                }
            });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sales_create);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));

        gridView = findViewById(R.id.gridView);
        layoutBtnThanhToan = findViewById(R.id.layoutBtnThanhToan);
        tvTotalQuantity = findViewById(R.id.tvTotalQuantityInCart);
        tvTotalAmount = findViewById(R.id.tvTotalPriceInCart);
        etSearch = findViewById(R.id.etSearch);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterProducts(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Do nothing
            }
        });

        loadProductList();

        cartItems = new ArrayList<>();

        btnTiepTuc = findViewById(R.id.btnTiepTuc);
        btnTiepTuc.setOnClickListener(v -> {
            Intent intent = new Intent(this, SaleConfirmActivity.class);
            intent.putExtra("cartItems", new ArrayList<>(cartItems));
            activityResultFinish.launch(intent);
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
                        productList.clear();
                        productList.addAll(list);
                        filteredProductList.clear();
                        filteredProductList.addAll(list);
                        adapter = new SaleCreateAdapter(SaleCreateActivity.this, filteredProductList, layoutBtnThanhToan, tvTotalQuantity, tvTotalAmount, cartItems);
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

    private void filterProducts(String keyword) {
        filteredProductList.clear();
        if (keyword.isEmpty()) {
            filteredProductList.addAll(productList);
        } else {
            for (Product product : productList) {
                if (product.getProductName().toLowerCase().contains(keyword.toLowerCase())) {
                    filteredProductList.add(product);
                }
            }
        }
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }
}