package com.example.doanmonhoc.activity.StaffManagement;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.doanmonhoc.R;
import com.example.doanmonhoc.api.KiotApiService;
import com.example.doanmonhoc.model.Staff;

import java.text.SimpleDateFormat;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StaffDetailManagementActivity extends AppCompatActivity {
    private TextView txtMaNV, txtName, txtDob, txtGender, txtAddress, txtEmail, txtPhone, txtUsername;
    private ImageView staffImage;

    private ImageButton btnBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_detail);

        txtMaNV = findViewById(R.id.maNV);
        txtName = findViewById(R.id.txtName);
        txtDob = findViewById(R.id.txtDob);
        txtGender = findViewById(R.id.txtGender);
        txtAddress = findViewById(R.id.txtAddress);
        txtEmail = findViewById(R.id.txtEmail);
        txtPhone = findViewById(R.id.txtPhone);
        txtUsername = findViewById(R.id.txtUsername);
        staffImage = findViewById(R.id.staffImage);
        btnBack = findViewById(R.id.btnBack); // Initialize btnBack

        // Nhận ID nhân viên từ Intent
        long staffId = getIntent().getLongExtra("staffId", -1);

        // Gọi API để lấy thông tin nhân viên theo ID
        KiotApiService.apiService.getStaffById(staffId).enqueue(new Callback<Staff>() {
            @Override
            public void onResponse(Call<Staff> call, Response<Staff> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Staff staff = response.body();
                    displayStaffDetails(staff);
                } else {
                    showErrorMessage();
                }
            }

            @Override
            public void onFailure(Call<Staff> call, Throwable t) {
                // Xử lý khi gọi API không thành công
                showErrorMessage();
                t.printStackTrace();
            }
        });

        // Xử lý sự kiện khi nhấn nút back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Đóng activity hiện tại
            }
        });
    }

    private void displayStaffDetails(Staff staff) {
        // Hiển thị thông tin nhân viên trên giao diện
        txtMaNV.setText(staff.getStaffKey());
        txtName.setText(staff.getStaffName());

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        txtDob.setText(dateFormat.format(staff.getStaffDob()));

        txtGender.setText(staff.getStaffGender() == 1 ? "Nam" : "Nữ");
        txtAddress.setText(staff.getAddress());
        txtEmail.setText(staff.getStaffEmail());
        txtPhone.setText(staff.getStaffPhone());
        txtUsername.setText(staff.getUsername());

        // Hiển thị ảnh nhân viên nếu có
        String imageName = staff.getStaffImage(); // Assuming 'getStaffImage()' returns image file name
        int resourceId = getResources().getIdentifier(imageName, "drawable", getPackageName());

        Glide.with(this)
                .load(resourceId)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .into(staffImage);
    }

    private void showErrorMessage() {
        Toast.makeText(this, "Không tìm thấy thông tin nhân viên", Toast.LENGTH_SHORT).show();
        finish();
    }
}
