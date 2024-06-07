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
import java.text.ParseException;
import java.text.SimpleDateFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.Locale;

public class AccountDetailActivity extends AppCompatActivity {
    private TextView maNV, txtName, txtDob, txtGender, txtAddress, txtEmail, txtPhone;
    private Gson gson;
    private ShapeableImageView staffImage;

    // Khởi tạo ActivityResultLauncher
    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                loadAccountDetail();
            }
        });
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

        gson = new Gson();

        Button btnEdit = findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(v -> btnEditOnClick());

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

                    if (staff.getStaffImage() != null && !staff.getStaffImage().isEmpty()) {
                        int resID = getResources().getIdentifier(staff.getStaffImage(), "drawable", getPackageName());
                        staffImage.setImageResource(resID);
                        staffImage.setTag(staff.getStaffImage()); // Store the resource name in the tag
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

    private void btnEditOnClick() {
        Staff staff = new Staff();
        SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        staff.setId(sharedPreferences.getLong("id", -1));
        staff.setStaffKey(maNV.getText().toString());
        staff.setStaffName(txtName.getText().toString());
        staff.setStaffEmail(txtEmail.getText().toString());
        staff.setStaffPhone(txtPhone.getText().toString());
        staff.setAddress(txtAddress.getText().toString());

        // set Dob
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            staff.setStaffDob(dateFormat.parse(txtDob.getText().toString()));
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(this, "Định dạng ngày tháng không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        // set gender
        Byte gender = txtGender.getText().toString().equalsIgnoreCase("Nam") ? (byte) 1 : (byte) 0;
        staff.setStaffGender(gender);

        //set image
        if (staffImage.getTag() != null) {
            staff.setStaffImage(staffImage.getTag().toString());
        }

        String staffJson = gson.toJson(staff);

        Intent intent = new Intent(AccountDetailActivity.this, EditAccountActivity.class);
        intent.putExtra("staff", staffJson);
        activityResultLauncher.launch(intent);
    }
}
