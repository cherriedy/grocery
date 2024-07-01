package com.example.doanmonhoc.activity.ImportManagement;

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
import com.example.doanmonhoc.activity.Main.MainActivity;
import com.example.doanmonhoc.activity.SaleManagement.SaleCreateActivity;
import com.example.doanmonhoc.activity.SaleManagement.SaleDetailedInvoiceActivity;
import com.example.doanmonhoc.adapter.ImportBillAdapter;
import com.example.doanmonhoc.adapter.SaleDetailedInvoiceAdapter;
import com.example.doanmonhoc.api.KiotApiService;
import com.example.doanmonhoc.model.DetailedGoodsReceivedNote;
import com.example.doanmonhoc.model.DetailedInvoice;
import com.example.doanmonhoc.model.Product;
import com.example.doanmonhoc.model.Staff;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImportBillActivity extends AppCompatActivity {
    private ImportBillAdapter adapter;
    private ListView listView;
    private ImageButton btnBack;
    private Button btnExit;
    private TextView tvGrnKey, staffName, createAt, totalPrice, tvNote;
    private List<DetailedGoodsReceivedNote> detailedGoodsReceivedNotes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_import_bill);


        listView = findViewById(R.id.listView);
        tvGrnKey = findViewById(R.id.tvGrnKey);
        totalPrice = findViewById(R.id.totalPrice);
        staffName = findViewById(R.id.tvStaffName);
        createAt = findViewById(R.id.tvCreateAt);
        tvNote = findViewById(R.id.tvNote);

        adapter = new ImportBillAdapter(this, detailedGoodsReceivedNotes);
        listView.setAdapter(adapter);

        Intent intent = getIntent();
        long grnId = intent.getLongExtra("grnId", -1);
        String grnKey = intent.getStringExtra("grnKey");
        long staffId = intent.getLongExtra("staffId", -1);
        long createdAtMillis = intent.getLongExtra("createAt", -1);
        // Reconstruct Timestamp from long
        Timestamp createdAt = new Timestamp(createdAtMillis);


        double totalAmount = intent.getDoubleExtra("totalAmount", 0);
        String note = intent.getStringExtra("note");

        // Display tv
        tvGrnKey.setText(grnKey);
        getStaffNameById(staffId);
        createAt.setText(createdAt.toString());

        totalPrice.setText(String.valueOf(totalAmount));
        tvNote.setText(note);

        loadDetailedInvoiceById(grnId);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        btnExit = findViewById(R.id.btnExit);
        btnExit.setOnClickListener(v -> {
            Intent intent1 = new Intent(this, MainActivity.class);
            startActivity(intent1);
            finishAffinity();
        });
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
                Toast.makeText(ImportBillActivity.this, "Không lấy tên nhân viên được!", Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", "Loi error staff name", t);
            }
        });
    }

    private void loadDetailedInvoiceById(long grnId) {
        KiotApiService.apiService.getDetailedGoodsReceivedNote(grnId).enqueue(new Callback<List<DetailedGoodsReceivedNote>>() {
            @Override
            public void onResponse(Call<List<DetailedGoodsReceivedNote>> call, Response<List<DetailedGoodsReceivedNote>> response) {
                if (response.isSuccessful()) {
                    List<DetailedGoodsReceivedNote> list = response.body();
                    if (list != null && !list.isEmpty()) {
                        detailedGoodsReceivedNotes.clear();
                        detailedGoodsReceivedNotes.addAll(list);

                        for (DetailedGoodsReceivedNote detail : detailedGoodsReceivedNotes) {
                            KiotApiService.apiService.getDetailedProduct(detail.getProductId()).enqueue(new Callback<Product>() {
                                @Override
                                public void onResponse(Call<Product> call, Response<Product> response) {
                                    if (response.isSuccessful()) {
                                        Product product = response.body();
                                        detail.setProduct(product);

                                        // Notify adapter only when all products not null
                                        if (allProductsNotNull(detailedGoodsReceivedNotes)) {
                                            adapter.notifyDataSetChanged();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<Product> call, Throwable t) {
                                    Toast.makeText(ImportBillActivity.this, "Không setProduct được!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } else {
                        Toast.makeText(ImportBillActivity.this, "Không tìm thấy chi tiết hóa đơn nào!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ImportBillActivity.this, "Không có phản hồi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<DetailedGoodsReceivedNote>> call, Throwable t) {
                Toast.makeText(ImportBillActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private boolean allProductsNotNull(List<DetailedGoodsReceivedNote> detailedGoodsReceivedNotes) {
        for (DetailedGoodsReceivedNote detail : detailedGoodsReceivedNotes) {
            if (detail.getProduct() == null) {
                return false;
            }
        }
        return true;
    }
}
