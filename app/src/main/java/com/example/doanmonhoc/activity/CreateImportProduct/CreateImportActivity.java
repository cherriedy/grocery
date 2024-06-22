    package com.example.doanmonhoc.activity.CreateImportProduct;

    import static com.example.doanmonhoc.api.KiotApiService.okHttpClient;

    import android.content.Intent;
    import android.os.Bundle;
    import android.util.Log;
    import android.view.View;
    import android.widget.ListView;

    import androidx.appcompat.app.AppCompatActivity;

    import com.example.doanmonhoc.R;
    import com.example.doanmonhoc.adapter.ListCreateImport;
    import com.example.doanmonhoc.api.KiotApiService;
    import com.example.doanmonhoc.model.DetailedGoodsReceivedNote;
    import com.example.doanmonhoc.model.GoodsReceivedNote;
    import com.example.doanmonhoc.model.Product;

    import java.util.ArrayList;
    import java.util.List;

    import retrofit2.Call;
    import retrofit2.Callback;
    import retrofit2.Response;
    import retrofit2.Retrofit;
    import retrofit2.converter.gson.GsonConverterFactory;

    public class CreateImportActivity extends AppCompatActivity {

        private ListView listView;
        private ListCreateImport adapter;
        private List<DetailedGoodsReceivedNote> detailList = new ArrayList<>();
        private List<Product> productList = new ArrayList<>();
        private List<GoodsReceivedNote> grnList = new ArrayList<>();

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_import_create);

            listView = findViewById(R.id.list_view);
            adapter = new ListCreateImport(this, detailList, productList, grnList);
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
            apiService.getProductList().enqueue(new Callback<List<Product>>() {
                @Override
                public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                    if (response.isSuccessful()) {
                        List<Product> products = response.body();
                        productList.clear();
                        productList.addAll(products);
                        adapter.notifyDataSetChanged();
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
            Intent intent = new Intent(this, DetailImportActivity.class);
            startActivity(intent);
        }

        public void ReturntoManagementImportActivity(View view) {
            Intent intent = new Intent(this, ManagementImportActivity.class);
            startActivity(intent);
        }
    }
