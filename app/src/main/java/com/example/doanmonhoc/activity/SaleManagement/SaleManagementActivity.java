package com.example.doanmonhoc.activity.SaleManagement;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.example.doanmonhoc.R;
import com.example.doanmonhoc.adapter.SaleManagementAdapter;
import com.example.doanmonhoc.api.KiotApiService;
import com.example.doanmonhoc.model.Invoice;
import com.example.doanmonhoc.model.Product;
import com.example.doanmonhoc.model.Staff;
import com.google.android.material.radiobutton.MaterialRadioButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SaleManagementActivity extends AppCompatActivity {
    private ListView listView;
    private SaleManagementAdapter adapter;
    private Button btnAddInvoice;
    private ImageButton btnBack;
    private EditText etSearch;
    private List<Invoice> invoiceList = new ArrayList<>();
    private List<Invoice> filteredInvoiceList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sales_management);

        listView = findViewById(R.id.listView);
        etSearch = findViewById(R.id.etSearch);

        btnAddInvoice = findViewById(R.id.btnAddInvoice);
        btnAddInvoice.setOnClickListener(v -> {
            Intent intent = new Intent(this, SaleCreateActivity.class);
            startActivity(intent);
        });

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        // Set up the TextWatcher for search functionality
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterInvoices(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Do nothing
            }
        });

        loadAllInvoice();
    }

    private void loadAllInvoice() {
        KiotApiService.apiService.getAllInvoice().enqueue(new Callback<List<Invoice>>() {
            @Override
            public void onResponse(Call<List<Invoice>> call, Response<List<Invoice>> response) {
                if (response.isSuccessful()) {
                    List<Invoice> list = response.body();
                    if (list != null && !list.isEmpty()) {
                        invoiceList.clear();
                        invoiceList.addAll(list);
                        filteredInvoiceList.clear();
                        filteredInvoiceList.addAll(list);
                        adapter = new SaleManagementAdapter(SaleManagementActivity.this, filteredInvoiceList);
                        listView.setAdapter(adapter);
                    } else {
                        Toast.makeText(SaleManagementActivity.this, "Không tìm thấy sản phẩm nào!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SaleManagementActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Invoice>> call, Throwable t) {
                Toast.makeText(SaleManagementActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterInvoices(String keyword) {
        filteredInvoiceList.clear();
        if (keyword.isEmpty()) {
            filteredInvoiceList.addAll(invoiceList);
        } else {
            for (Invoice invoice : invoiceList) {
                if (invoice.getInvoiceKey().toLowerCase().contains(keyword.toLowerCase())) {
                    filteredInvoiceList.add(invoice);
                }
            }
        }
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAllInvoice(); // Tải lại danh sách
    }
}
