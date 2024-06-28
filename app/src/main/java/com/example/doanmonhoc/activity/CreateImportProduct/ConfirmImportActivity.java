package com.example.doanmonhoc.activity.CreateImportProduct;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.doanmonhoc.R;
import com.example.doanmonhoc.activity.SaleManagement.SaleConfirmActivity;
import com.example.doanmonhoc.activity.SaleManagement.SaleDetailedInvoiceActivity;
import com.example.doanmonhoc.adapter.ConfirmImportAdapter;
import com.example.doanmonhoc.adapter.SaleConfirmAdapter;
import com.example.doanmonhoc.api.KiotApiService;
import com.example.doanmonhoc.model.CartItem;
import com.example.doanmonhoc.model.GoodsReceivedNote;
import com.example.doanmonhoc.model.ImportItem;
import com.example.doanmonhoc.model.Invoice;
import com.example.doanmonhoc.model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfirmImportActivity extends AppCompatActivity {
private List<ImportItem> importItems;
    private ListView listView;
    private TextView tvTongTien, tvTongSoLuong, tvTongGiam, tvThanhTien;

    private EditText etNote;
    private ConfirmImportAdapter adapter;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_import_confirm);

   //     tvTongTien = findViewById(R.id.tv_totalPrice);
     //   tvTongSoLuong = findViewById(R.id.tv_totalquantity);
     //   tvThanhTien = findViewById(R.id.tv_totalPrice1);
     //   etNote = findViewById(R.id.note);


        importItems = getIntent().getSerializableExtra("importItems",ArrayList.class);
        listView = findViewById(R.id.lv_select);
        adapter = new ConfirmImportAdapter(this, importItems,tvTongTien, tvTongSoLuong,tvThanhTien);
        listView.setAdapter(adapter);

    //  updateTotalUI();

      ImageButton btnBack = findViewById(R.id.backButton);
        btnBack.setOnClickListener(v -> finish());

       Button btnimport = findViewById(R.id.btn_import);
        btnimport.setOnClickListener(v -> {
            GoodsReceivedNote goodsReceivedNote = new GoodsReceivedNote();
            SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
            long staffId = sharedPreferences.getLong("id", -1);
            goodsReceivedNote.setStaffid(staffId);
            goodsReceivedNote.setTotalAmount(Double.parseDouble(tvThanhTien.getText().toString().replace(" đ", "").replace(",", "")));
            goodsReceivedNote.setNote(etNote.getText().toString());

            List<ImportItem> simplifiedImportItem = new ArrayList<>();
            for (ImportItem importItem : importItems) {
                ImportItem simplifiedItem = new ImportItem(importItem.getProductId(), importItem.getQuantity(), importItem.getPrice());
                simplifiedImportItem.add(simplifiedItem);
            }
            goodsReceivedNote.setImportItems(simplifiedImportItem);

            addimportgoods(goodsReceivedNote);
        });
    }

    private void updateTotalUI() {
        int totalQuantity = 0;
        double totalAmount = 0;
        double totalDiscount = 0;
        double originalTotal = 0;

        for (ImportItem item : importItems) {
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
        tvTongTien.setText(String.format("%.2f đ", 6));
        tvTongGiam.setText(String.format("%.2f đ", 8));
        tvThanhTien.setText(String.format("%.2f đ", 8));
    }

    private void addimportgoods(GoodsReceivedNote goodsReceivedNote) {
        KiotApiService.apiService.getcrGoodsReceivedNote(goodsReceivedNote).enqueue(new Callback<GoodsReceivedNote>() {
            @Override
            public void onResponse(Call<GoodsReceivedNote> call, Response<GoodsReceivedNote> response) {
                GoodsReceivedNote addedImport = response.body();
                if (addedImport != null) {
                    Intent intent = new Intent(ConfirmImportActivity.this, BillImportActivity.class);
                    intent.putExtra("importId", addedImport.getId());
                    intent.putExtra("importKey", addedImport.getGrnKey());
                    intent.putExtra("staffId", addedImport.getStaffid());
                    intent.putExtra("createAt", addedImport.getCreatedAt().getTime());
                    intent.putExtra("totalAmount", addedImport.getTotalAmount());
                    intent.putExtra("note", addedImport.getNote());
                    startActivity(intent);
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(ConfirmImportActivity.this, "Không tìm thấy sản phẩm nào!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GoodsReceivedNote> call, Throwable t) {
                Toast.makeText(ConfirmImportActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", "Loi error ko load dc", t);
            }
        });
    }

}
