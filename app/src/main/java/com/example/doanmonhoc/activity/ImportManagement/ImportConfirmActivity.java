package com.example.doanmonhoc.activity.ImportManagement;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.doanmonhoc.R;
import com.example.doanmonhoc.adapter.ImportConfirmAdapter;
import com.example.doanmonhoc.api.KiotApiService;
import com.example.doanmonhoc.model.CartItem;
import com.example.doanmonhoc.model.DetailedGoodsReceivedNote;
import com.example.doanmonhoc.model.GoodsReceivedNote;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImportConfirmActivity extends AppCompatActivity {
    private List<DetailedGoodsReceivedNote> cartItems;
    private ListView listView;
    private ImportConfirmAdapter adapter;
    private TextView tvThanhTien, tvTongSoLuong;
    private EditText etNote;
    private ImageButton btnBack;
    private Button btnNhapHang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_confirm);

        tvThanhTien = findViewById(R.id.tvThanhTien);
        tvTongSoLuong = findViewById(R.id.tvTongSoLuong);
        etNote = findViewById(R.id.note);
        listView = findViewById(R.id.listView);


        cartItems = getIntent().getSerializableExtra("cartItems", ArrayList.class);
        adapter = new ImportConfirmAdapter(this, cartItems, tvTongSoLuong, tvThanhTien);
        listView.setAdapter(adapter);
        updateTotalUI();

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        btnNhapHang = findViewById(R.id.btnNhapHang);
        btnNhapHang.setOnClickListener(v -> {
            GoodsReceivedNote goodsReceivedNote = new GoodsReceivedNote();
            SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
            long staffId = sharedPreferences.getLong("id", -1);
            goodsReceivedNote.setStaffid(staffId);
            goodsReceivedNote.setTotalAmount(Double.parseDouble(tvThanhTien.getText().toString().replace(" đ", "").replace(",", "")));
            goodsReceivedNote.setNote(etNote.getText().toString());

            List<DetailedGoodsReceivedNote> simplifiedCartItems = new ArrayList<>();
            for (DetailedGoodsReceivedNote cartItem : cartItems) {
                DetailedGoodsReceivedNote simplifiedItem = new DetailedGoodsReceivedNote(cartItem.getProductId(), cartItem.getQuantity(), cartItem.getPrice());
                simplifiedCartItems.add(simplifiedItem);
            }
            goodsReceivedNote.setCartItems(simplifiedCartItems);

            addGoodReceivedNote(goodsReceivedNote);
        });
    }

    private void updateTotalUI() {
        int totalQuantity = 0;
        double totalAmount = 0;

        for (DetailedGoodsReceivedNote item : cartItems) {
            int quantity = item.getQuantity();
            double inPrice = item.getPrice();

            totalQuantity += quantity;
            totalAmount += inPrice;
        }

        tvTongSoLuong.setText(totalQuantity + " sản phẩm");
        tvThanhTien.setText(String.format("%.2f đ", totalAmount));
    }

    private void addGoodReceivedNote(GoodsReceivedNote goodsReceivedNote) {
        KiotApiService.apiService.addGoodReceivedNote(goodsReceivedNote).enqueue(new Callback<GoodsReceivedNote>() {
            @Override
            public void onResponse(Call<GoodsReceivedNote> call, Response<GoodsReceivedNote> response) {
                GoodsReceivedNote addedGrn = response.body();
                if (addedGrn != null) {
                    Intent intent = new Intent(ImportConfirmActivity.this, ImportBillActivity.class);
                    intent.putExtra("grnId", addedGrn.getId());
                    intent.putExtra("grnKey", addedGrn.getGrnKey());
                    intent.putExtra("staffId", addedGrn.getStaffid());
                    intent.putExtra("createAt", addedGrn.getCreatedAt().getTime());
                    intent.putExtra("totalAmount", addedGrn.getTotalAmount());
                    intent.putExtra("note", addedGrn.getNote());
                    startActivity(intent);
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(ImportConfirmActivity.this, "Không tìm thấy sản phẩm nào!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GoodsReceivedNote> call, Throwable t) {
                Toast.makeText(ImportConfirmActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", "Loi error ko load dc", t);
            }
        });
    }
}