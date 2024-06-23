package com.example.doanmonhoc.activity.CreateImportProduct;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.doanmonhoc.R;
import com.example.doanmonhoc.adapter.ConfirmImportAdapter;
import com.example.doanmonhoc.model.Product;

import java.util.ArrayList;

public class ConfirmImportActivity extends AppCompatActivity {

    private ListView listView;
    private ConfirmImportAdapter adapter;
    private ArrayList<Product> selectedProducts;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_confirm);

        selectedProducts = (ArrayList<Product>) getIntent().getSerializableExtra("selectedProducts");

        listView = findViewById(R.id.lv_select);
        adapter = new ConfirmImportAdapter(this, selectedProducts);
        listView.setAdapter(adapter);

        Button btnAddProduct = findViewById(R.id.btn_add);
        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGoodsImportActivity();
            }
        });
    }

    private void openGoodsImportActivity() {
        Intent intent = new Intent(this, GoodsImportActivity.class);
        intent.putExtra("selectedProducts", selectedProducts);
        startActivity(intent);
        finish();
    }



    public void navigateToBillImportActivity(View view) {
        Intent intent = new Intent(this, BillImportActivity.class);
        startActivity(intent);
    }
    public void ReturnToCreateImportActivity(View view) {
        Intent intent = new Intent(this, GoodsImportActivity.class);
        startActivity(intent);
    }
}
