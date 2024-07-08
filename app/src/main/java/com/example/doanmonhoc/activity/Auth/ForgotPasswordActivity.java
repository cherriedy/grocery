package com.example.doanmonhoc.activity.Auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.doanmonhoc.R;
import com.example.doanmonhoc.api.KiotApiService;
import com.example.doanmonhoc.api.MailjetApiService;
import com.example.doanmonhoc.model.OTP;
import com.example.doanmonhoc.model.OTPRequest;
import com.example.doanmonhoc.model.OTPResponse;
import com.example.doanmonhoc.model.Staff;

import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {
    public static final String API_KEY = "bc134a134d9bee09055d5d51b68ae499";
    public static final String API_SECRET = "75c88599be4e25d590fa560f71e29e00";

    private Button btnSendOTP, btnTiepTuc;
    private EditText editEmail, editOTP;
    private String generatedOTP, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));

        editEmail = findViewById(R.id.editPhoneNumber);
        editOTP = findViewById(R.id.editOTP);
        btnSendOTP = findViewById(R.id.btnSendOTP);
        btnTiepTuc = findViewById(R.id.btnTiepTuc);

        btnSendOTP.setOnClickListener(v -> {
            email = editEmail.getText().toString();
            if (email.isEmpty()) {
                Toast.makeText(this, "Hãy nhập email!", Toast.LENGTH_SHORT).show();
            } else {
                checkEmail(email);
            }
        });

        btnTiepTuc.setOnClickListener(v -> {
            String enteredOTP = editOTP.getText().toString();
            if (enteredOTP.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please enter the OTP", Toast.LENGTH_SHORT).show();
            } else {
                verifyOTP(email, enteredOTP);
            }
        });
    }

    private void checkEmail(String email) {
        KiotApiService.apiService.getStaffByEmail(email).enqueue(new Callback<Staff>() {
            @Override
            public void onResponse(Call<Staff> call, Response<Staff> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Generate OTP
                    generatedOTP = String.valueOf((int) (Math.random() * 9000) + 1000);
                    OTP otp = new OTP(generatedOTP);
                    updateOTP(email, otp);
                } else {
                    Toast.makeText(ForgotPasswordActivity.this, "Email không khớp", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Staff> call, Throwable throwable) {
                Toast.makeText(ForgotPasswordActivity.this, "Email không hợp lệ!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateOTP(String email, OTP otp) {
        KiotApiService.apiService.updateStaffByEmail(email, otp).enqueue(new Callback<OTP>() {
            @Override
            public void onResponse(Call<OTP> call, Response<OTP> response) {
                if (response.isSuccessful() && response.body() != null) {
                    sendOTP(email, String.valueOf(otp));
                } else {
                    Toast.makeText(ForgotPasswordActivity.this, "Failed update OTP trong db.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OTP> call, Throwable t) {
                Toast.makeText(ForgotPasswordActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendOTP(String email, String otp) {
        // Send OTP via email using Mailjet
        OTPRequest.From from = new OTPRequest.From("yennhisociuu2004@gmail.com", "7Eleven - Cửa hàng tiện lợi");
        OTPRequest.To to = new OTPRequest.To(email);
        OTPRequest.Message message = new OTPRequest.Message(
                from,
                Collections.singletonList(to),
                "Mã Xác Thực OTP",
                "Mã OTP của bạn là: " + otp,
                "<p>Mã OTP (One Time Password) của bạn là: " + otp + "</p>"
        );

        OTPRequest request = new OTPRequest(Collections.singletonList(message));
        MailjetApiService.api.sendEmail(request).enqueue(new Callback<OTPResponse>() {
            @Override
            public void onResponse(Call<OTPResponse> call, Response<OTPResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    OTPResponse otpResponse = response.body();
                    if (!otpResponse.getMessages().isEmpty() &&
                            "success".equalsIgnoreCase(otpResponse.getMessages().get(0).getStatus())) {
                        Toast.makeText(ForgotPasswordActivity.this, "Đã gửi mã OTP. Hãy check email!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ForgotPasswordActivity.this, "Error: Unable to send OTP", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ForgotPasswordActivity.this, "Failed to send OTP", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OTPResponse> call, Throwable t) {
                Toast.makeText(ForgotPasswordActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void verifyOTP(String email, String enteredOTP) {
        OTP otp = new OTP(email, enteredOTP);
        KiotApiService.apiService.verifyOTP(otp).enqueue(new Callback<OTP>() {
            @Override
            public void onResponse(Call<OTP> call, Response<OTP> response) {
                if (response.isSuccessful() && response.body() != null) {
                    OTP otp1 = response.body();
                    if (otp1.isSuccess()) {
                        Toast.makeText(ForgotPasswordActivity.this, "OTP xác thực thành công!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ForgotPasswordActivity.this, ChangePasswordActivity.class);
                        intent.putExtra("email", otp1.getEmail());
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(ForgotPasswordActivity.this, otp1.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<OTP> call, Throwable t) {
                Toast.makeText(ForgotPasswordActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
