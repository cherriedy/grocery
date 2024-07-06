package com.example.doanmonhoc.activity.AccountManagement;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;


import com.example.doanmonhoc.R;
import com.example.doanmonhoc.api.KiotApiService;
import com.example.doanmonhoc.model.Staff;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.Locale;

public class AccountDetailActivity extends AppCompatActivity {
    private TextView maNV, txtName, txtDob, txtGender, txtAddress, txtEmail, txtPhone, txtUsername;
    private ShapeableImageView staffImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account_detail);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        maNV = findViewById(R.id.maNV);
        txtName = findViewById(R.id.txtName);
        txtDob = findViewById(R.id.txtDob);
        txtGender = findViewById(R.id.txtGender);
        txtAddress = findViewById(R.id.txtAddress);
        txtEmail = findViewById(R.id.txtEmail);
        txtPhone = findViewById(R.id.txtPhone);
        staffImage = findViewById(R.id.staffImage);
        txtUsername = findViewById(R.id.txtUsername);

        Button btnEdit = findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(v -> btnEditOnClick());

        Button btnPassword = findViewById(R.id.btnPassword);
        btnPassword.setOnClickListener(v -> btnPasswordOnClick());

        loadAccountDetail();
    }
    private void loadAccountDetail() {
        SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        long staffId = sharedPreferences.getLong("id", -1);

        if (staffId == -1) {
            Toast.makeText(this, "Không tìm thấy Staff ID trong SharedPreferences", Toast.LENGTH_SHORT).show();
            return;
        }

        KiotApiService.apiService.getStaffById(staffId).enqueue(new Callback<Staff>() {
            @Override
            public void onResponse(Call<Staff> call, Response<Staff> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Staff staff = response.body();
                    maNV.setText(staff.getStaffKey());
                    txtName.setText(staff.getStaffName());

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    txtDob.setText(dateFormat.format(staff.getStaffDob()));

                    txtGender.setText(staff.getStaffGender() == 1 ? "Nam" : "Nữ");
                    txtAddress.setText(staff.getAddress());
                    txtEmail.setText(staff.getStaffEmail());
                    txtPhone.setText(staff.getStaffPhone());
                    txtUsername.setText(staff.getUsername());

                    // Load staff image if available
                    if (staff.getStaffImage() != null && !staff.getStaffImage().isEmpty()) {
                        Picasso.get().load(staff.getStaffImage()).into(staffImage);
                    }
                } else {
                    Toast.makeText(AccountDetailActivity.this, "Failed to get staff info", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Staff> call, Throwable t) {
                Toast.makeText(AccountDetailActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void btnPasswordOnClick() {
        Intent intent = new Intent(AccountDetailActivity.this, EditPasswordActivity.class);
        startActivity(intent);
    }

    private void btnEditOnClick() {
        Intent intent = new Intent(AccountDetailActivity.this, EditAccountActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAccountDetail(); // Tải lại danh sách
    }
}
