package com.example.doanmonhoc.activity.CreateImportProduct;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.doanmonhoc.R;
import com.example.doanmonhoc.adapter.BillImportAdapter;
import com.example.doanmonhoc.adapter.ConfirmImportAdapter;
import com.example.doanmonhoc.model.Product;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class BillImportActivity extends AppCompatActivity {

    private ArrayList<Product> selectedProducts;
    private ListView listView;
    private BillImportAdapter adapter;
    private String totalPrice;
    private String billID;
    @SuppressLint("MissingInflatedId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_bill);
        selectedProducts = (ArrayList<Product>) getIntent().getSerializableExtra("selectedProducts");

        listView = findViewById(R.id.item_listselect);
        adapter = new BillImportAdapter(this, selectedProducts);
        listView.setAdapter(adapter);


        ImageButton btnAddProduct = findViewById(R.id.backButton);
        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateConfirmImportActivity();
            }
        });

        billID = generateRandomID();
        TextView billIDTextView = findViewById(R.id.order_code);
        billIDTextView.setText(billID);

        double totalPrice = getIntent().getDoubleExtra("totalPrice", 0.0);
        TextView totalTextView = findViewById(R.id.item_total);
        totalTextView.setText(String.format("%.2f", totalPrice));

        TextView totalTextView1 = findViewById(R.id.item_total1);
        totalTextView1.setText(String.format("%.2f", totalPrice));

        String currentTime = getTime();
        TextView currentTimeTextView = findViewById(R.id.tv_time);
        currentTimeTextView.setText(currentTime);

        String currentDay = getDay();
        TextView currentDayTextView = findViewById(R.id.tv_day);
        currentDayTextView.setText(currentDay);


    }
    public void navigateToManagementImportActivity(View view) {
        Intent intent = new Intent(this, ManagementImportActivity.class);
        startActivity(intent);
    }
    public void navigateConfirmImportActivity() {
        Intent intent = new Intent(this, ConfirmImportActivity.class);
        intent.putExtra("selectedProducts", selectedProducts);
        startActivity(intent);
    }

    private String generateRandomID() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder id = new StringBuilder();
        Random random = new Random();
        int length = 6;  // Độ dài của mã ID
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            id.append(characters.charAt(index));
        }
        return id.toString();
    }

    private String getTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss ", Locale.getDefault());
        return sdf.format(new Date());
    }

    private String getDay() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy ", Locale.getDefault());
        return sdf.format(new Date());
    }



}


