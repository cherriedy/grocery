package com.example.doanmonhoc.activity.AccountManagement;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.example.doanmonhoc.model.Account;
import com.example.doanmonhoc.model.Staff;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AccountDetailActivity extends AppCompatActivity {

    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account_detail);

        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        TextView maNV = findViewById(R.id.maNV);
        TextView txtName = findViewById(R.id.txtName);
        TextView txtDob = findViewById(R.id.txtDob);
        TextView txtGender = findViewById(R.id.txtGender);
        TextView txtAddress = findViewById(R.id.txtAddress);
        TextView txtEmail = findViewById(R.id.txtEmail);
        TextView txtPhone = findViewById(R.id.txtPhone);

        Button btnEdit = findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(v -> {
            Staff staff = new Staff();
            staff.setStaffKey(maNV.getText().toString());
            staff.setStaffName(txtName.getText().toString());
            staff.setStaffEmail(txtEmail.getText().toString());
            staff.setStaffPhone(txtPhone.getText().toString());
            staff.setAddress(txtAddress.getText().toString());

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date date = sdf.parse(txtDob.getText().toString());
                staff.setStaffDob(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Byte gender = txtGender.getText().toString().equals("Nam") ? (byte) 1 : (byte) 0;
            staff.setStaffGender(gender);

            String staffJson = gson.toJson(staff);

            Intent intent = new Intent(AccountDetailActivity.this, EditAccountActivity.class);
            intent.putExtra("staff", staffJson);
            startActivity(intent);
        });

        loadAccountDetail(maNV, txtName, txtDob, txtEmail, txtPhone, txtAddress, txtGender);
    }

    private void loadAccountDetail(TextView maNV, TextView txtName, TextView txtDob, TextView txtEmail, TextView txtPhone, TextView txtAddress, TextView txtGender) {
        SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        long staffId = sharedPreferences.getLong("id", -1);

        if (staffId == -1) {
            Toast.makeText(this, "Không tìm thấy Staff ID trong SharedPreferences", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = RetrofitClient.getApiService(this);
        Call<Staff> call = apiService.getStaffById(staffId);
        call.enqueue(new Callback<Staff>() {
            @Override
            public void onResponse(Call<Staff> call, Response<Staff> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Staff staff = response.body();
                    maNV.setText(staff.getStaffKey());
                    txtName.setText(staff.getStaffName());

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String formattedDate = sdf.format(staff.getStaffDob());
                    txtDob.setText(formattedDate);

                    txtGender.setText(staff.getStaffGender() == 1 ? "Nam" : "Nữ");
                    txtAddress.setText(staff.getAddress());
                    txtEmail.setText(staff.getStaffEmail());
                    txtPhone.setText(staff.getStaffPhone());
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
}
