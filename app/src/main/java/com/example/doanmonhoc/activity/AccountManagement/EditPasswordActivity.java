package com.example.doanmonhoc.activity.AccountManagement;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.doanmonhoc.R;
import com.example.doanmonhoc.activity.Auth.LoginActivity;
import com.example.doanmonhoc.api.KiotApiService;
import com.example.doanmonhoc.model.Staff;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditPasswordActivity extends AppCompatActivity {
    private EditText mkcu, mkm1, mkm2;
    private Button btnDmk;
    private ImageButton btnBack;
    private Staff staff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));

        mkcu = findViewById(R.id.mkcu);
        mkm1 = findViewById(R.id.mkm1);
        mkm2 = findViewById(R.id.mkm2);
        btnDmk = findViewById(R.id.btnDmk);
        btnBack = findViewById(R.id.btnBack);

        SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        long staffId = sharedPreferences.getLong("id", -1);
        if (staffId != -1) {
            fetchStaffDetails(staffId);
        } else {
            Toast.makeText(this, "Không tìm thấy Staff ID trong SharedPreferences", Toast.LENGTH_SHORT).show();
            finish();
        }

        btnBack.setOnClickListener(v -> finish());
        btnDmk.setOnClickListener(v -> changePassword());
    }

    private void fetchStaffDetails(long staffId) {
        KiotApiService.apiService.getStaffById(staffId).enqueue(new Callback<Staff>() {
            @Override
            public void onResponse(Call<Staff> call, Response<Staff> response) {
                if (response.isSuccessful()) {
                    staff = response.body();
                } else {
                    Toast.makeText(EditPasswordActivity.this, "Không tìm thấy thông tin nhân viên", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Staff> call, Throwable throwable) {
                Toast.makeText(EditPasswordActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void changePassword() {
        if (staff == null) {
            Toast.makeText(this, "Không tìm thấy thông tin nhân viên", Toast.LENGTH_SHORT).show();
            return;
        }

        String matkhaucu = mkcu.getText().toString();
        String matkhaumoi1 = mkm1.getText().toString();
        String matkhaumoi2 = mkm2.getText().toString();

        if (TextUtils.isEmpty(matkhaucu) || TextUtils.isEmpty(matkhaumoi1) || TextUtils.isEmpty(matkhaumoi2)) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!matkhaucu.equals(staff.getPassword())) {
            Toast.makeText(this, "Mật khẩu cũ không chính xác", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!matkhaumoi1.equals(matkhaumoi2)) {
            Toast.makeText(this, "Mật khẩu mới không khớp", Toast.LENGTH_SHORT).show();
            return;
        }

        staff.setPassword(matkhaumoi1);

        KiotApiService.apiService.updateStaff(staff.getId(), staff).enqueue(new Callback<Staff>() {
            @Override
            public void onResponse(Call<Staff> call, Response<Staff> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EditPasswordActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EditPasswordActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finishAffinity();
                } else {
                    Toast.makeText(EditPasswordActivity.this, "Cập nhật thất bại: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Staff> call, Throwable throwable) {
                Toast.makeText(EditPasswordActivity.this, "Cập nhật thất bại: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
