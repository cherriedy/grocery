package com.example.doanmonhoc.activity.Auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.doanmonhoc.R;
import com.example.doanmonhoc.activity.AccountManagement.EditPasswordActivity;
import com.example.doanmonhoc.api.KiotApiService;
import com.example.doanmonhoc.model.ChangePasswordRequest;
import com.example.doanmonhoc.model.Staff;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {
    private EditText editNewPass, editNewPass1;
    private Button btnChangePass;
    private Staff staff;
    private String staffEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);

        editNewPass = findViewById(R.id.editNewPass);
        editNewPass1 = findViewById(R.id.editNewPass1);

        staffEmail = getIntent().getStringExtra("email");

        btnChangePass = findViewById(R.id.btnChangePass);
        btnChangePass.setOnClickListener(v -> {
            getStaff(staffEmail);
            updatePass();
        });

    }
    private void getStaff(String staffEmail){
        KiotApiService.apiService.getStaffByEmail(staffEmail).enqueue(new Callback<Staff>() {
            @Override
            public void onResponse(Call<Staff> call, Response<Staff> response) {
                if (response.isSuccessful()) {
                    staff = response.body();
                } else {
                    Toast.makeText(ChangePasswordActivity.this, "Không tìm thấy thông tin nhân viên", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Staff> call, Throwable throwable) {
                Toast.makeText(ChangePasswordActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void updatePass(){
        String newPass = editNewPass.getText().toString();
        String newPass1 = editNewPass1.getText().toString();

        if (TextUtils.isEmpty(newPass) || TextUtils.isEmpty(newPass1)) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
        }else if(!newPass.equals(newPass1)){
            Toast.makeText(this, "Mật khẩu mới không khớp", Toast.LENGTH_SHORT).show();
        }

        staff.setPassword(newPass);
        KiotApiService.apiService.updateStaff(staff.getId(), staff).enqueue(new Callback<Staff>() {
            @Override
            public void onResponse(Call<Staff> call, Response<Staff> response) {
                if (response.isSuccessful()) {
                    Intent intent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                    startActivity(intent);
                    Toast.makeText(ChangePasswordActivity.this, "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                    finishAffinity();
                } else {
                    Toast.makeText(ChangePasswordActivity.this, "Cập nhật thất bại: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Staff> call, Throwable throwable) {
                Toast.makeText(ChangePasswordActivity.this, "Cập nhật thất bại: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
