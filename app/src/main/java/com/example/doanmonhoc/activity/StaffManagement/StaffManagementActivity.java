package com.example.doanmonhoc.activity.StaffManagement;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.doanmonhoc.R;
import com.example.doanmonhoc.adapter.StaffAdapter;
import com.example.doanmonhoc.api.KiotApiService;
import com.example.doanmonhoc.model.Staff;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StaffManagementActivity extends AppCompatActivity implements StaffAdapter.OnDeleteClickListener {

    private ListView listView;
    private List<Staff> staffList;
    private StaffAdapter adapter;
    private ImageButton back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_management);

        listView = findViewById(R.id.listViewStaff);
        back_btn = findViewById(R.id.back_btn);

        fetchStaffList();

        back_btn.setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchStaffList(); // Tải lại danh sách nhân viên khi quay lại màn hình này
    }

    private void fetchStaffList() {
        KiotApiService.apiService.getAllStaff().enqueue(new Callback<List<Staff>>() {
            @Override
            public void onResponse(Call<List<Staff>> call, Response<List<Staff>> response) {
                if (response.isSuccessful()) {
                    staffList = response.body();
                    adapter = new StaffAdapter(StaffManagementActivity.this, staffList, StaffManagementActivity.this);
                    listView.setAdapter(adapter);
                } else {
                    Toast.makeText(StaffManagementActivity.this, "Lấy danh sách nhân viên thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Staff>> call, Throwable t) {
                Toast.makeText(StaffManagementActivity.this, "Lỗi mạng! Vui lòng thử lại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDeleteClick(long staffId) {
        showDeleteConfirmationDialog(staffId);
    }

    private void showDeleteConfirmationDialog(long staffId) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xoá")
                .setMessage("Bạn có chắc chắn muốn xoá nhân viên này không?")
                .setPositiveButton("Có", (dialog, which) -> deleteStaff(staffId))
                .setNegativeButton("Không", null)
                .show();
    }

    private void deleteStaff(long staffId) {
        KiotApiService.apiService.deleteStaff(staffId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(StaffManagementActivity.this, "Xoá nhân viên thành công", Toast.LENGTH_SHORT).show();
                    fetchStaffList(); // Làm mới danh sách sau khi xoá thành công
                } else {
                    Toast.makeText(StaffManagementActivity.this, "Xoá nhân viên thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(StaffManagementActivity.this, "Lỗi mạng! Vui lòng thử lại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showStaffDetail(long staffId) {
        Intent intent = new Intent(StaffManagementActivity.this, StaffDetailManagementActivity.class);
        intent.putExtra("staffId", staffId);
        startActivity(intent);
    }
}
