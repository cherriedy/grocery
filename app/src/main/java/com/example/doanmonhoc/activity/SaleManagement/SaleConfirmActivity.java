package com.example.doanmonhoc.activity.SaleManagement;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.doanmonhoc.R;
import com.example.doanmonhoc.adapter.SaleConfirmAdapter;
import com.example.doanmonhoc.adapter.SaleDetailedInvoiceAdapter;
import com.example.doanmonhoc.adapter.SaleManagementAdapter;
import com.example.doanmonhoc.api.KiotApiService;
import com.example.doanmonhoc.model.CartItem;
import com.example.doanmonhoc.model.DetailedInvoice;
import com.example.doanmonhoc.model.Invoice;
import com.example.doanmonhoc.model.Product;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SaleConfirmActivity extends AppCompatActivity {
    private List<CartItem> cartItems;
    private ListView listView;
    private SaleConfirmAdapter adapter;
    private TextView tvTongTien, tvTongSoLuong, tvTongGiam, tvThanhTien;
    private EditText etNote;
    private ImageButton btnBack;
    private Button btnThanhToan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_confirm);

        tvTongTien = findViewById(R.id.tvTongTien);
        tvTongSoLuong = findViewById(R.id.tvTongSoLuong);
        tvTongGiam = findViewById(R.id.tvTongGiam);
        tvThanhTien = findViewById(R.id.tvThanhTien);
        etNote = findViewById(R.id.note);
        listView = findViewById(R.id.listView);


        cartItems = getIntent().getSerializableExtra("cartItems", ArrayList.class);
        adapter = new SaleConfirmAdapter(this, cartItems, tvTongTien, tvTongSoLuong,tvTongGiam, tvThanhTien);
        listView.setAdapter(adapter);
        updateTotalUI();

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        btnThanhToan = findViewById(R.id.btnThanhToan);
        btnThanhToan.setOnClickListener(v -> {
            Invoice invoice = new Invoice();
            SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
            long staffId = sharedPreferences.getLong("id", -1);
            invoice.setStaffid(staffId);
            invoice.setTotalAmount(Double.parseDouble(tvThanhTien.getText().toString().replace(" đ", "")));
            invoice.setNote(etNote.getText().toString());

            List<CartItem> simplifiedCartItems = new ArrayList<>();
            for (CartItem cartItem : cartItems) {
                CartItem simplifiedItem = new CartItem(cartItem.getProductId(), cartItem.getQuantity(), cartItem.getPrice());
                simplifiedCartItems.add(simplifiedItem);
            }
            invoice.setCartItems(simplifiedCartItems);

            addInvoice(invoice);
        });
    }

    private void updateTotalUI() {
        int totalQuantity = 0;
        int totalAmount = 0;
        int totalDiscount = 0;
        int originalTotal = 0;

        for (CartItem item : cartItems) {
            int quantity = item.getQuantity();
            Product product = item.getProduct();
            double originalPrice = product.getOutPrice() * quantity;
            double discountedPrice = item.getPrice();
            double discountAmount = originalPrice - discountedPrice;

            totalQuantity += quantity;
            originalTotal += originalPrice;
            totalAmount += discountedPrice;
            totalDiscount += discountAmount;
        }

        tvTongSoLuong.setText(totalQuantity + " sản phẩm");
        tvTongTien.setText(originalTotal + " đ");
        tvTongGiam.setText(totalDiscount + " đ");
        tvThanhTien.setText(totalAmount + " đ");
    }

    private void addInvoice(Invoice invoice) {
        KiotApiService.apiService.addInvoice(invoice).enqueue(new Callback<Invoice>() {
            @Override
            public void onResponse(Call<Invoice> call, Response<Invoice> response) {
                Invoice addedInvoice = response.body();
                if (addedInvoice != null) {
                    Intent intent = new Intent(SaleConfirmActivity.this, SaleDetailedInvoiceActivity.class);
                    intent.putExtra("invoiceId", addedInvoice.getId());
                    intent.putExtra("invoiceKey", addedInvoice.getInvoiceKey());
                    intent.putExtra("staffId", addedInvoice.getStaffid());
                    intent.putExtra("createAt", addedInvoice.getCreatedAt().getTime());
                    intent.putExtra("totalAmount", addedInvoice.getTotalAmount());
                    intent.putExtra("note", addedInvoice.getNote());
                    startActivity(intent);
                    setResult(RESULT_OK);
                    finish();
                } else {
                        Toast.makeText(SaleConfirmActivity.this, "Không tìm thấy sản phẩm nào!", Toast.LENGTH_SHORT).show();
                    }
                }

            @Override
            public void onFailure(Call<Invoice> call, Throwable t) {
                Toast.makeText(SaleConfirmActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", "Loi error ko load dc", t);
            }
        });
    }
}