package com.example.doanmonhoc.activity.CreateImportProduct;

import static com.example.doanmonhoc.api.KiotApiService.okHttpClient;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.doanmonhoc.R;
import com.example.doanmonhoc.activity.SaleManagement.SaleConfirmActivity;
import com.example.doanmonhoc.activity.SaleManagement.SaleCreateActivity;
import com.example.doanmonhoc.adapter.ListFoodsImportAdapter;
import com.example.doanmonhoc.api.KiotApiService;
import com.example.doanmonhoc.model.DetailedGoodsReceivedNote;
import com.example.doanmonhoc.model.GoodsReceivedNote;
import com.example.doanmonhoc.model.ImportItem;
import com.example.doanmonhoc.model.Product;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GoodsImportActivity extends AppCompatActivity {
    private List<ImportItem> importItems;
    private ListView listView;
    private ListFoodsImportAdapter adapter;
    private List<Product> productList = new ArrayList<>();

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
        setContentView(R.layout.activity_import_create);

        listView = findViewById(R.id.list_view);

        fetchDataFromApi();

        importItems = new ArrayList<>();

       Button btnTiepTuc = findViewById(R.id.btnTiepTuc);
        btnTiepTuc.setOnClickListener(v -> {
            Intent intent = new Intent(this,ConfirmImportActivity.class);
            intent.putExtra("cartItems", new ArrayList<>(importItems));
            activityResultFinish.launch(intent);
        });
    }

    private void fetchDataFromApi() {
        KiotApiService.apiService.getProductList().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    List<Product> products = response.body();
                    productList.clear();
                    productList.addAll(products);
                    adapter = new ListFoodsImportAdapter(GoodsImportActivity.this, productList, importItems);
                    listView.setAdapter(adapter);
                } else {
                    Log.e("API Error", "Server returned error: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.e("API Error", "Network error: " + t.getMessage());
            }
        });
    }

    public void navigateToDetailImportActivity(View view) {
        Intent intent = new Intent(this, ConfirmImportActivity.class);

        intent.putExtra("cartItems", new ArrayList<>(importItems));
        activityResultFinish.launch(intent);
        startActivity(intent);
    }

    public void ReturntoManagementImportActivity(View view) {
        Intent intent = new Intent(this, ManagementImportActivity.class);
        startActivity(intent);
    }
}
