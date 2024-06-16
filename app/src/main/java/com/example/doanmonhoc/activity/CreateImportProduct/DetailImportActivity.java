package com.example.doanmonhoc.activity.CreateImportProduct;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.doanmonhoc.R;

public class DetailImportActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_confirm);
    }

    public void navigateToBillImportActivity(View view) {
        Intent intent = new Intent(this, BillImportActivity.class);
        startActivity(intent);
    }
    public void ReturnToCreateImportActivity(View view) {
        Intent intent = new Intent(this, CreateImportActivity.class);
        startActivity(intent);
    }
}
