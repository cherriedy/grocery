package com.example.doanmonhoc.activity.AccountManagement;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.doanmonhoc.R;
import com.example.doanmonhoc.RetrofitClient;
import com.example.doanmonhoc.api.ApiService;
import com.example.doanmonhoc.api.KiotApiService;
import com.example.doanmonhoc.model.Staff;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class EditAccountActivity extends AppCompatActivity {

    private EditText txtName, txtDob, txtGender, txtAddress, txtEmail, txtPhone;
    private Button btnBack, btnSave;
    private Staff staff;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account_edit);

        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

        String staffJson = getIntent().getStringExtra("staff");
        staff = gson.fromJson(staffJson, Staff.class);

        txtName = findViewById(R.id.txtName);
        txtDob = findViewById(R.id.txtDob);
        txtGender = findViewById(R.id.txtGender);
        txtAddress = findViewById(R.id.txtAddress);
        txtEmail = findViewById(R.id.txtEmail);
        txtPhone = findViewById(R.id.txtPhone);

        txtName.setText(staff.getStaffName());
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        txtDob.setText(sdf.format(staff.getStaffDob()));
//        LocalDate staffDob =

        txtGender.setText(staff.getStaffGender() == 1 ? "Nam" : "Nữ");
        txtAddress.setText(staff.getAddress());
        txtEmail.setText(staff.getStaffEmail());
        txtPhone.setText(staff.getStaffPhone());

        btnBack = findViewById(R.id.btnCancel);
        btnBack.setOnClickListener(v -> finish());

        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(v -> updateStaff());
    }

    private void updateStaff() {
        String newName = txtName.getText().toString().trim();
        String newDob = txtDob.getText().toString();
        boolean newGender = txtGender.getText().toString().equals("Nam");
        String newAddress = txtAddress.getText().toString();
        String newEmail = txtEmail.getText().toString();
        String newPhone = txtPhone.getText().toString();

        if (TextUtils.isEmpty(newName)) {
            Toast.makeText(this, "Vui lòng nhập tên", Toast.LENGTH_SHORT).show();
            return;
        }

        staff.setStaffName(newName);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = sdf.parse(newDob);
            staff.setStaffDob(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        staff.setStaffGender(newGender ? (byte) 1 : (byte) 0);
        staff.setStaffEmail(newEmail);
        staff.setAddress(newAddress);
        staff.setStaffPhone(newPhone);

//        ApiService apiService = RetrofitClient.getApiService(this);
//        Call<Staff> call = apiService.updateStaff(staff.getId(), staff);

        KiotApiService.apiService.updateStaff(staff.getId(), staff).enqueue(new Callback<Staff>() {
            @Override
            public void onResponse(Call<Staff> call, Response<Staff> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EditAccountActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(EditAccountActivity.this, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Staff> call, Throwable throwable) {
                Toast.makeText(EditAccountActivity.this, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
