package com.example.doanmonhoc.activity.Auth;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.doanmonhoc.R;

import java.util.List;
import java.util.Random;

import android.Manifest;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class ForgotPasswordActivity extends AppCompatActivity {

    Button btnSendOTP, btnTiepTuc;
    EditText editPhoneNumber, editOTP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);

        editPhoneNumber = findViewById(R.id.editPhoneNumber);
        editOTP = findViewById(R.id.editOTP);
        btnSendOTP = findViewById(R.id.btnSendOTP);
        btnTiepTuc = findViewById(R.id.btnTiepTuc);

        btnSendOTP.setOnClickListener(v -> {
            String phoneNumber = editPhoneNumber.getText().toString();
            if (phoneNumber.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please enter phone number", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
