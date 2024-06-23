package com.example.doanmonhoc.activity.Main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.doanmonhoc.R;
import com.example.doanmonhoc.activity.AccountManagement.AccountDetailActivity;
import com.example.doanmonhoc.activity.Auth.LoginActivity;
import com.example.doanmonhoc.databinding.ActivityMainBinding;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private SharedPreferences mPerfs;
    private String mAccountName;
    private String mAccountRole;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.primaryColor));

        binding.appBar.imageAvatarToggle.setOnClickListener(v -> binding.main.open());

        mPerfs = getSharedPreferences("myPrefs", MODE_PRIVATE);
        getAccountInformation();
        setNavigationHeader();
        setAppbarText();

        replaceFragment(new HomepageFragment());
        handleNavigationView();
    }

    @Override
    public void onBackPressed() {
        if (binding.main.isOpen()) {
            binding.main.close();
        } else {
            super.onBackPressed();
        }
    }

    private void logout() {
        SharedPreferences.Editor editor = getSharedPreferences("myPrefs", MODE_PRIVATE).edit();
        editor.remove("id");
        editor.remove("Roleid");
        editor.remove("staffName");
        editor.apply();

        Toast.makeText(this, "Đăng xuất thành công!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, LoginActivity.class);
        // Xóa tất cả các activity trước đó khỏi stack
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        // Kết thúc tất cả các Activity hiện tại để người dùng không thể quay lại bằng nút back
        finishAffinity();
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content, fragment);
        fragmentTransaction.commit();
    }

    private void handleNavigationView() {
        binding.navigationView.setNavigationItemSelectedListener(item -> {
            Map<Integer, Integer> navigationItem = new HashMap<>();
            navigationItem.put(R.id.nav_account_setting, 0);
            navigationItem.put(R.id.nav_logout, 1);
            try {
                Integer currentItemId = navigationItem.get(item.getItemId());
                switch (currentItemId) {
                    case 0:
                        startActivity(new Intent(this, AccountDetailActivity.class));
                        break;
                    case 1:
                        logout();
                        break;
                }
            } catch (Exception e) {
                binding.main.close();
                return false;
            }
            binding.main.close();
            return true;
        });
    }

    private void getAccountInformation() {
        mAccountName = mPerfs.getString("staffName", "NULL");
        switch ((int) mPerfs.getLong("Roleid", 1)) {
            case 1:
                mAccountRole = "Quản trị viên";
                break;
            case 2:
                mAccountRole = "Nhân viên";
                break;
        }
    }

    private void setNavigationHeader() {
        View navigationView = LayoutInflater
                .from(this)
                .inflate(R.layout.nav_header_main, binding.navigationView, false);

        TextView textAccountName = navigationView.findViewById(R.id.text_name);
        TextView textAccountRole = navigationView.findViewById(R.id.text_role);
        textAccountName.setText(mAccountName);
        textAccountRole.setText(mAccountRole);
        binding.navigationView.addHeaderView(navigationView);
    }

    private void setAppbarText() {
        binding.appBar.textName.setText(mAccountName);
        binding.appBar.textRole.setText(mAccountRole);
    }
}
