package com.example.doanmonhoc.activity.CreateImportProduct;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.doanmonhoc.R;
import com.example.doanmonhoc.adapter.ConfirmImportAdapter;
import com.example.doanmonhoc.model.Product;

import java.util.ArrayList;
import java.util.Random;

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

        Button btnSubmit = findViewById(R.id.btn_import);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBillImportActivity();
            }
        });

        double total = calculateTotal();
        TextView totalTextView = findViewById(R.id.tv_totalPrice);
        totalTextView.setText(String.format("%.2f", total));

        TextView totalTextView1 = findViewById(R.id.tv_totalPrice1);
        totalTextView1.setText(String.format("%.2f", total));

        Integer totalquantity = updateTotalQuantity();
        TextView totalTextView2 = findViewById(R.id.tv_totalquantity);
        totalTextView2.setText(String.format("%d", totalquantity));
    }

    private void openGoodsImportActivity() {
        Intent intent = new Intent(this, GoodsImportActivity.class);
        intent.putExtra("selectedProducts", selectedProducts);
        startActivity(intent);
        finish();
    }

    private void openBillImportActivity() {
        double totalPrice = calculateTotal();
        Intent intent = new Intent(this, BillImportActivity.class);
        intent.putExtra("selectedProducts", selectedProducts);
        intent.putExtra("totalPrice", totalPrice);
        startActivity(intent);
        finish();
    }

    public void ReturnToCreateImportActivity(View view) {
        Intent intent = new Intent(this, GoodsImportActivity.class);
        startActivity(intent);
    }

    private double calculateTotal() {
        double total = 0;
        for (Product product : selectedProducts) {
            total += product.getActualQuantity() * product.getInPrice();
        }
        return total;
    }

    private Integer updateTotalQuantity() {
        int totalQuantity = 0;
        for (Product product : selectedProducts) {
            totalQuantity += product.getActualQuantity();
        }
       return totalQuantity;
    }

}
