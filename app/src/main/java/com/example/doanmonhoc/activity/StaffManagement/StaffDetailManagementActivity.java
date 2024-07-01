package com.example.doanmonhoc.activity.StaffManagement;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
    private Button btnEdit, btnDelete;

    private long staffId;

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
        btnBack = findViewById(R.id.btnBack);
        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);

        // Nhận ID nhân viên từ Intent
        staffId = getIntent().getLongExtra("staffId", -1);

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

        // Xử lý sự kiện khi nhấn nút edit
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StaffDetailManagementActivity.this, StaffEditActivity.class);
                intent.putExtra("staffId", staffId);
                startActivity(intent);
            }
        });

        // Xử lý sự kiện khi nhấn nút delete
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hiển thị dialog xác nhận xóa
                showDeleteConfirmationDialog();
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

    private void deleteStaff() {
        KiotApiService.apiService.deleteStaff(staffId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(StaffDetailManagementActivity.this, "Xóa nhân viên thành công", Toast.LENGTH_SHORT).show();
                    // Trở về StaffManagementActivity và làm mới danh sách nhân viên
                    Intent intent = new Intent(StaffDetailManagementActivity.this, StaffManagementActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish(); // Đóng activity hiện tại
                } else {
                    Toast.makeText(StaffDetailManagementActivity.this, "Xóa nhân viên thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(StaffDetailManagementActivity.this, "Xảy ra lỗi khi xóa nhân viên", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Bạn có chắc chắn muốn xóa nhân viên này?")
                .setCancelable(false)
                .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Xác nhận đồng ý xóa
                        deleteStaff();
                    }
                })
                .setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Đóng dialog khi người dùng hủy bỏ
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void showErrorMessage() {
        Toast.makeText(this, "Không tìm thấy thông tin nhân viên", Toast.LENGTH_SHORT).show();
        finish();
    }
}
