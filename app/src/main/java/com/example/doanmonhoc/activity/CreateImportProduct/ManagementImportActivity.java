package com.example.doanmonhoc.activity.CreateImportProduct;

import static com.example.doanmonhoc.api.KiotApiService.okHttpClient;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.doanmonhoc.R;
import com.example.doanmonhoc.adapter.ListManagementImport;
import com.example.doanmonhoc.api.KiotApiService;
import com.example.doanmonhoc.model.DetailedGoodsReceivedNote;
import com.example.doanmonhoc.model.GoodsReceivedNote;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ManagementImportActivity extends AppCompatActivity {

    private ListView listView;
    private ListManagementImport adapter;
    private List<DetailedGoodsReceivedNote> detailList = new ArrayList<>();
    private List<GoodsReceivedNote> grnList = new ArrayList<>();

   @SuppressLint("MissingInflatedId")
   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_management);

        listView = findViewById(R.id.lv_Mangement);
        adapter = new ListManagementImport(this, grnList, detailList);
        listView.setAdapter(adapter);

        fetchDataFromApi();
    }

    private void fetchDataFromApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://cherrapi.onlinewebshop.net")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        KiotApiService apiService = retrofit.create(KiotApiService.class);
        apiService.getGoodsReceivedNoteList().enqueue(new Callback<List<GoodsReceivedNote>>() {
            @Override
            public void onResponse(Call<List<GoodsReceivedNote>> call, Response<List<GoodsReceivedNote>> response) {
                if (response.isSuccessful()) {
                    List<GoodsReceivedNote> goodsReceivedNotes = response.body();
                    goodsReceivedNotes.clear();
                    goodsReceivedNotes.addAll(goodsReceivedNotes);
                    adapter.notifyDataSetChanged();
                } else {
                    Log.e("API Error", "Server returned error: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<GoodsReceivedNote>> call, Throwable t) {
                Log.e("API Error", "Network error: " + t.getMessage());
            }
        });
    }

    public void navigateToCreateImportActivity(View view) {
        Intent intent = new Intent(this, CreateImportActivity.class);
        startActivity(intent);
    }
}
