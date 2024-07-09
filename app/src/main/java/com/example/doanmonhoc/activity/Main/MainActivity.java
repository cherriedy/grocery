package com.example.doanmonhoc.activity.Main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
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
import com.example.doanmonhoc.activity.AccountManagement.EditPasswordActivity;
import com.example.doanmonhoc.activity.Auth.LoginActivity;
import com.example.doanmonhoc.databinding.ActivityMainBinding;
import com.example.doanmonhoc.utils.PicassoHelper;
import com.example.doanmonhoc.utils.PrefsUtils;
import com.example.doanmonhoc.utils.TextUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = AppCompatActivity.class.getSimpleName();

    private ActivityMainBinding binding;
    private SharedPreferences mPrefs;
    private String mAccountName;
    private int mAccountRoleId;
    private String mAccountRoleName;
    private String mAccountAvatar;

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

        mPrefs = getSharedPreferences("myPrefs", MODE_PRIVATE);
        getAccountInformation();
        setNavigationHeader();
        setAppbarText();

        handleNavigationView();
        switch (mAccountRoleId) {
            case 1:
                replaceFragment(new HomepageFragment());
                break;
            case 2:
                replaceFragment(new StaffHomepageFragment());
                break;
        }
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
        editor.remove(PrefsUtils.PREFS_ID);
        editor.remove(PrefsUtils.PREFS_ROLE);
        editor.remove(PrefsUtils.PREFS_NAME);
        editor.remove(PrefsUtils.PREFS_IMAGE);
        editor.apply();

        Toast.makeText(this, "Đăng xuất thành công!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, LoginActivity.class);
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
            navigationItem.put(R.id.nav_forget_password, 1);
            navigationItem.put(R.id.nav_logout, 2);

            Integer currentItemId = navigationItem.get(item.getItemId());
            if (currentItemId != null) {
                switch (currentItemId) {
                    case 0:
                        startActivity(new Intent(this, AccountDetailActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(this, EditPasswordActivity.class));
                        break;
                    case 2:
                        logout();
                        break;
                }
            } else {
                binding.main.close();
                return false;
            }
            binding.main.close();
            return true;
        });
    }

    private void getAccountInformation() {
        mAccountAvatar = mPrefs.getString("staffImage", "");
        mAccountName = mPrefs.getString("staffName", "");
        switch (mAccountRoleId = (int) mPrefs.getLong("Roleid", 1)) {
            case 1:
                mAccountRoleName = "Quản trị viên";
                break;
            case 2:
                mAccountRoleName = "Nhân viên";
                break;
        }
    }

    private void setNavigationHeader() {
        View navigationView = LayoutInflater
                .from(this)
                .inflate(R.layout.nav_header_main, binding.navigationView, false);

        TextView textAccountName = navigationView.findViewById(R.id.text_name);
        TextView textAccountRole = navigationView.findViewById(R.id.text_role);
        ImageView imageAccountAvatar = navigationView.findViewById(R.id.image_avatar);
        textAccountName.setText(mAccountName);
        textAccountRole.setText(mAccountRoleName);

        if (TextUtils.isValidString(mAccountAvatar)) {
            PicassoHelper.getPicassoInstance(this)
                    .load(mAccountAvatar)
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            imageAccountAvatar.setImageBitmap(bitmap);
                            binding.appBar.imageAvatarToggle.setImageBitmap(bitmap);
                        }

                        @Override
                        public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                            imageAccountAvatar.setImageResource(R.drawable.img_no_image);
                            binding.appBar.imageAvatarToggle.setImageResource(R.drawable.img_no_image);
                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {
                            imageAccountAvatar.setImageResource(R.drawable.img_no_image);
                            binding.appBar.imageAvatarToggle.setImageResource(R.drawable.img_no_image);
                        }
                    });
        }

        binding.navigationView.addHeaderView(navigationView);
    }

    private void setAppbarText() {
        binding.appBar.textName.setText(mAccountName);
        binding.appBar.textRole.setText(mAccountRoleName);
    }
}
