package com.example.doanmonhoc.activity.StaffManagement;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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
    private ImageButton add_btn;
    private EditText etSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_management);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));

        listView = findViewById(R.id.listViewStaff);
        back_btn = findViewById(R.id.back_btn);
        add_btn = findViewById(R.id.add_button);
        etSearch = findViewById(R.id.etSearch); // Thêm EditText để nhập từ khóa tìm kiếm

        fetchStaffList();

        back_btn.setOnClickListener(v -> finish());
        add_btn.setOnClickListener(v -> goToAddStaff());

        // Xử lý sự kiện khi người dùng nhập từ khóa tìm kiếm
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (adapter != null) {
                    adapter.filter(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
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

    private void goToAddStaff() {
        Intent intent = new Intent(StaffManagementActivity.this, AddStaffActivity.class);
        startActivity(intent);
    }
}
