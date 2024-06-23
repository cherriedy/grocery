package com.example.doanmonhoc.activity.Auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.doanmonhoc.R;

public class ForgotPasswordActivity extends AppCompatActivity {
    private Button btnMa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);

        btnMa = findViewById(R.id.btnMa);

        btnMa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPasswordActivity.this, ChangePasswordActivity.class);
                // Start the new activity
                startActivity(intent);
            }
        });
    }
}
