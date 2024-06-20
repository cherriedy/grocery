package com.example.doanmonhoc.activity.SaleManagement;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.doanmonhoc.R;
import com.example.doanmonhoc.adapter.SaleDetailedInvoiceAdapter;
import com.example.doanmonhoc.adapter.SaleManagementAdapter;
import com.example.doanmonhoc.api.KiotApiService;
import com.example.doanmonhoc.model.DetailedInvoice;
import com.example.doanmonhoc.model.Invoice;
import com.example.doanmonhoc.model.Product;
import com.example.doanmonhoc.model.Staff;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SaleDetailedInvoiceActivity extends AppCompatActivity {
    private SaleDetailedInvoiceAdapter adapter;
    private ListView listView;
    private ImageButton btnBack;
    private Button btnAddInvoice;
    private TextView tvinvoiceKey, staffName, createAt, totalPrice, tvNote;
    private List<DetailedInvoice> detailedInvoices = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sales_bill);


        listView = findViewById(R.id.listView);
        tvinvoiceKey = findViewById(R.id.tvInvoiceKey);
        totalPrice = findViewById(R.id.totalPrice);
        staffName = findViewById(R.id.tvStaffName);
        createAt = findViewById(R.id.tvCreateAt);
        tvNote = findViewById(R.id.tvNote);

        adapter = new SaleDetailedInvoiceAdapter(this, detailedInvoices);
        listView.setAdapter(adapter);

        Intent intent = getIntent();
        long invoiceId = intent.getLongExtra("invoiceId", -1);
        String invoiceKey = intent.getStringExtra("invoiceKey");
        long staffId = intent.getLongExtra("staffId", -1);
        long createdAtMillis = intent.getLongExtra("createAt", -1);
        // Reconstruct Timestamp from long
        Timestamp createdAt = new Timestamp(createdAtMillis);


        double totalAmount = intent.getDoubleExtra("totalAmount", 0);
        String note = intent.getStringExtra("note");

        // Display tv
        tvinvoiceKey.setText(invoiceKey);
        getStaffNameById(staffId);
        createAt.setText(createdAt.toString());

        totalPrice.setText(String.valueOf(totalAmount));
        tvNote.setText(note);

        loadDetailedInvoiceById(invoiceId);

        btnAddInvoice = findViewById(R.id.btnAddInvoice);
        btnAddInvoice.setOnClickListener(v -> {
            Intent intent1 = new Intent(this, SaleCreateActivity.class);
            startActivity(intent1);
            finish();
        });

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());
    }
    private void getStaffNameById(long staffId){
        KiotApiService.apiService.getStaffById(staffId).enqueue(new Callback<Staff>() {
            @Override
            public void onResponse(Call<Staff> call, Response<Staff> response) {
                if (response.isSuccessful()) {
                    Staff staff = response.body();
                    staffName.setText(staff.getStaffName());
                }
            }

            @Override
            public void onFailure(Call<Staff> call, Throwable t) {
                Toast.makeText(SaleDetailedInvoiceActivity.this, "Không lấy tên nhân viên được!", Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", "Loi error staff name", t);
            }
        });
    }

    private void loadDetailedInvoiceById(long invoiceId) {
        KiotApiService.apiService.getDetailedInvoiceById(invoiceId).enqueue(new Callback<List<DetailedInvoice>>() {
            @Override
            public void onResponse(Call<List<DetailedInvoice>> call, Response<List<DetailedInvoice>> response) {
                if (response.isSuccessful()) {
                    List<DetailedInvoice> list = response.body();
                    if (list != null && !list.isEmpty()) {
                        detailedInvoices.clear();
                        detailedInvoices.addAll(list);

                        for (DetailedInvoice detail : detailedInvoices) {
                            KiotApiService.apiService.getDetailedProduct(detail.getProductId()).enqueue(new Callback<Product>() {
                                @Override
                                public void onResponse(Call<Product> call, Response<Product> response) {
                                    if (response.isSuccessful()) {
                                        Product product = response.body();
                                        detail.setProduct(product);

                                        // Notify adapter only when all products not null
                                        if (allProductsNotNull(detailedInvoices)) {
                                            adapter.notifyDataSetChanged();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<Product> call, Throwable t) {
                                    Toast.makeText(SaleDetailedInvoiceActivity.this, "Không setProduct được!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } else {
                        Toast.makeText(SaleDetailedInvoiceActivity.this, "Không tìm thấy chi tiết hóa đơn nào!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SaleDetailedInvoiceActivity.this, "Không có phản hồi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<DetailedInvoice>> call, Throwable t) {
                Toast.makeText(SaleDetailedInvoiceActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private boolean allProductsNotNull(List<DetailedInvoice> detailedInvoices) {
        for (DetailedInvoice detail : detailedInvoices) {
            if (detail.getProduct() == null) {
                return false;
            }
        }
        return true;
    }
}
