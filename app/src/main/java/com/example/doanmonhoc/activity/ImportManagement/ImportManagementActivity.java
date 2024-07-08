package com.example.doanmonhoc.activity.ImportManagement;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.doanmonhoc.R;
import com.example.doanmonhoc.adapter.ImportManagementAdapter;
import com.example.doanmonhoc.api.KiotApiService;
import com.example.doanmonhoc.model.GoodsReceivedNote;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImportManagementActivity extends AppCompatActivity {
    private ListView listView;
    private ImportManagementAdapter adapter;
    private Button btnCreateImport;
    private ImageButton btnBack;
    private EditText etSearch;
    private List<GoodsReceivedNote> importList = new ArrayList<>();
    private List<GoodsReceivedNote> filteredImportList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_import_management);

        listView = findViewById(R.id.listView);
        etSearch = findViewById(R.id.etSearch);

        btnCreateImport = findViewById(R.id.btnCreateImport);
        btnCreateImport.setOnClickListener(v -> {
            Intent intent = new Intent(this, ImportCreateActivity.class);
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
                filterImportList(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                filterImportList(s.toString());
            }
        });

        getAllListImport();
    }

    private void getAllListImport() {
        KiotApiService.apiService.getAllGoodsReceivedNote().enqueue(new Callback<List<GoodsReceivedNote>>() {
            @Override
            public void onResponse(Call<List<GoodsReceivedNote>> call, Response<List<GoodsReceivedNote>> response) {
                if (response.isSuccessful()) {
                    List<GoodsReceivedNote> list = response.body();
                    if (list != null && !list.isEmpty()) {
                        importList.clear();
                        importList.addAll(list);
                        filteredImportList.clear();
                        filteredImportList.addAll(list);
                        adapter = new ImportManagementAdapter(ImportManagementActivity.this, filteredImportList);
                        listView.setAdapter(adapter);
                    } else {
                        Toast.makeText(ImportManagementActivity.this, "Không tìm thấy sản phẩm nào!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ImportManagementActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<GoodsReceivedNote>> call, Throwable t) {
                Toast.makeText(ImportManagementActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterImportList(String keyword) {
        filteredImportList.clear();
        if (keyword.isEmpty()) {
            filteredImportList.addAll(importList);
        } else {
            for (GoodsReceivedNote goodsReceivedNote : importList) {
                if (goodsReceivedNote.getGrnKey().toLowerCase().contains(keyword.toLowerCase())) {
                    filteredImportList.add(goodsReceivedNote);
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
        getAllListImport(); // Tải lại danh sách
    }
}