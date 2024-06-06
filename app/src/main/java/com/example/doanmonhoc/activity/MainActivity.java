package com.example.doanmonhoc.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.doanmonhoc.R;
import com.example.doanmonhoc.activity.ProductManagement.ProductManagementActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Button btnProduct = findViewById(R.id.btnProduct);
        btnProduct.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ProductManagementActivity.class);
            startActivity(intent);
        });

        Button btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> logout());
    }

    private void logout() {
        SharedPreferences.Editor editor = getSharedPreferences("myPrefs", MODE_PRIVATE).edit();
        editor.remove("id");
        editor.remove("Roleid");
        editor.apply();

        Toast.makeText(this, "Đăng xuất thành công!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, LoginActivity.class);
        // Xóa tất cả các activity trước đó khỏi stack
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        // Kết thúc tất cả các Activity hiện tại để người dùng không thể quay lại bằng nút back
        finishAffinity();
    }
}
