package com.example.doanmonhoc.activity.StaffManagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanmonhoc.R;
import com.example.doanmonhoc.adapter.StaffListAdapter;
import com.example.doanmonhoc.databinding.ActivityStaffManagementBinding;
import com.example.doanmonhoc.model.Staff;

import java.util.ArrayList;
import java.util.List;

public class StaffManagementActivity extends AppCompatActivity {

    ActivityStaffManagementBinding binding;

    private Animation animRotateClockWise;
    private Animation animRotateAntiClockWise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setup ViewBinding
        binding = ActivityStaffManagementBinding.inflate(getLayoutInflater());
        // Set layout for activity
        setContentView(binding.getRoot());

        // Create adapter for staffList (RecyclerView)
        StaffListAdapter staffListAdapter = new StaffListAdapter(StaffManagementActivity.this);
        // Create LinearLayoutManager to manage items
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(StaffManagementActivity.this, RecyclerView.VERTICAL, false);
        // Set LayoutManager
        binding.staffList.setLayoutManager(linearLayoutManager);

        // Set data array for Adapter via getStaffListData method
        staffListAdapter.setData(getStaffListData());
        // Set Adapter for staffList
        binding.staffList.setAdapter(staffListAdapter);

        initializeAnimation();

        binding.addButton.setOnClickListener(v -> {
            Intent intent = new Intent(StaffManagementActivity.this, AddStaffActivity.class);
            startActivity(intent);
        });
    }

    private void initializeAnimation() {
        animRotateClockWise = AnimationUtils.loadAnimation(StaffManagementActivity.this, R.anim.rotate_clock_wise);
        animRotateAntiClockWise = AnimationUtils.loadAnimation(StaffManagementActivity.this, R.anim.rotate_anti_clock_wise);
    }

    private List<Staff> getStaffListData() {
        List<Staff> list = new ArrayList<>();

        list.add(new Staff("John Doe", "john.doe@example.com", R.drawable.staff));
        list.add(new Staff("Jane Smith", "jane.smith@example.com", R.drawable.staff));
        list.add(new Staff("Emily Davis", "emily.davis@example.com", R.drawable.staff));
        list.add(new Staff("Michael Brown", "michael.brown@example.com", R.drawable.staff));
        list.add(new Staff("Linda Johnson", "linda.johnson@example.com", R.drawable.staff));
        list.add(new Staff("James Wilson", "james.wilson@example.com", R.drawable.staff));
        list.add(new Staff("Olivia Lee", "olivia.lee@example.com", R.drawable.staff));

        return list;
    }
}
