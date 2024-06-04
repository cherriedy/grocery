// StaffManagementActivity.java
package com.example.doanmonhoc.activity.StaffManagement;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import com.example.doanmonhoc.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class StaffManagementActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_management);

        FloatingActionButton addButton = findViewById(R.id.add_button);
        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(StaffManagementActivity.this, AddStaffActivity.class);
            startActivity(intent);
        });
    }
}
