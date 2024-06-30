package com.example.doanmonhoc.activity.StaffManagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.doanmonhoc.R;
import com.example.doanmonhoc.adapter.StaffAdapter;
import com.example.doanmonhoc.api.KiotApiService;
import com.example.doanmonhoc.model.Staff;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StaffManagementActivity extends AppCompatActivity {

    private ListView listView;
    private List<Staff> staffList;
    private StaffAdapter adapter;

    private ImageButton back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_management);

        // Đảm bảo bạn đang tham chiếu đúng ID của ListView trong layout file
        listView = findViewById(R.id.listViewStaff);
        back_btn = findViewById(R.id.back_btn); // Initialize back_btn

        fetchStaffList();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Staff staff = staffList.get(position);
                long staffId = staff.getId();
                showStaffDetail(staffId);
            }
        });

        // Set click listener for back_btn
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Finish the current activity and return to the previous one
            }
        });
    }

    private void fetchStaffList() {
        KiotApiService.apiService.getAllStaff().enqueue(new Callback<List<Staff>>() {
            @Override
            public void onResponse(Call<List<Staff>> call, Response<List<Staff>> response) {
                if (response.isSuccessful()) {
                    staffList = response.body();
                    adapter = new StaffAdapter(StaffManagementActivity.this, staffList);
                    listView.setAdapter(adapter);
                } else {
                    Toast.makeText(StaffManagementActivity.this, "Failed to fetch staff list", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Staff>> call, Throwable t) {
                Toast.makeText(StaffManagementActivity.this, "Network error! Please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showStaffDetail(long staffId) {
        Intent intent = new Intent(StaffManagementActivity.this, StaffDetailManagementActivity.class);
        intent.putExtra("staffId", staffId); // Gửi ID nhân viên qua Intent
        startActivity(intent);
    }
}
