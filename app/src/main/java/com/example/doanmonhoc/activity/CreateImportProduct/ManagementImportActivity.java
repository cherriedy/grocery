package com.example.doanmonhoc.activity.CreateImportProduct;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.doanmonhoc.R;

public class ManagementImportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_management);
    }

    public void navigateToCreateImportActivity(View view) {
        Intent intent = new Intent(this, CreateImportActivity.class);
        startActivity(intent);
    }
}
