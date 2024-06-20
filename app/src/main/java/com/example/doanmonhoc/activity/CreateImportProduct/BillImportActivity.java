package com.example.doanmonhoc.activity.CreateImportProduct;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.doanmonhoc.R;

public class BillImportActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_bill);
    }

    public void navigateToManagementImportActivity(View view) {
        Intent intent = new Intent(this, ManagementImportActivity.class);
        startActivity(intent);
    }
    public void navigatetoDetailsActivity(View view) {
        Intent intent = new Intent(this, DetailImportActivity.class);
        startActivity(intent);
    }
    }


