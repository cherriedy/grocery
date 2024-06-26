// DetailedGoodsReceivedNoteActivity.java
package com.example.doanmonhoc.activity.CreateImportProduct;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.doanmonhoc.R;
import com.example.doanmonhoc.adapter.DetailedGoodsReceivedNoteAdapter;
import com.example.doanmonhoc.api.KiotApiService;
import com.example.doanmonhoc.model.DetailedGoodsReceivedNote;
import com.example.doanmonhoc.model.Product;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailedGoodsReceivedNoteActivity extends AppCompatActivity {

    private ListView listView;
    private DetailedGoodsReceivedNoteAdapter adapter;
    private List<DetailedGoodsReceivedNote> detailedGoodsReceivedNotes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_good_import);

        listView = findViewById(R.id.lv_details);

        int goodsReceivedNoteId = getIntent().getIntExtra("goodsReceivedNoteId", -1);
        if (goodsReceivedNoteId != -1) {
            fetchDataFromApi(goodsReceivedNoteId);
        } else {
            Log.e("Detail Error", "No goodsReceivedNoteId passed to activity");
        }
    }

    private void fetchDataFromApi(int goodsReceivedNoteId) {
        KiotApiService.apiService.getDetailedGoodsReceivedNoteList(goodsReceivedNoteId).enqueue(new Callback<List<DetailedGoodsReceivedNote>>() {
            @Override
            public void onResponse(Call<List<DetailedGoodsReceivedNote>> call, Response<List<DetailedGoodsReceivedNote>> response) {
                if (response.isSuccessful()) {
                    List<DetailedGoodsReceivedNote> list = response.body();
                    detailedGoodsReceivedNotes.clear();
                    detailedGoodsReceivedNotes.addAll(list);

                    adapter = new DetailedGoodsReceivedNoteAdapter(DetailedGoodsReceivedNoteActivity.this, detailedGoodsReceivedNotes);
                    listView.setAdapter(adapter);

                    fetchProductDetails();
                } else {
                    Log.e("API Error", "Server returned error: " + response.errorBody());
                }
            }
            @Override
            public void onFailure(Call<List<DetailedGoodsReceivedNote>> call, Throwable t) {
                Log.e("API Error", "Network error: " + t.getMessage());
            }
        });
    }

    private void fetchProductDetails() {
        for (DetailedGoodsReceivedNote note : detailedGoodsReceivedNotes) {
            KiotApiService.apiService.getDetailedProduct(note.getProductid()).enqueue(new Callback<Product>() {
                @Override
                public void onResponse(Call<Product> call, Response<Product> response) {
                    if (response.isSuccessful()) {
                        Product product = response.body();
                        note.setProduct(product);
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.e("API Error", "Server returned error: " + response.errorBody());
                    }
                }
                @Override
                public void onFailure(Call<Product> call, Throwable t) {
                    Log.e("API Error", "Network error: " + t.getMessage());
                }
            });
        }
    }

    public void navigateToManagement(View view) {
        Intent intent = new Intent(this, ManagementImportActivity.class);
        startActivity(intent);
    }
}
