package com.example.doanmonhoc.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.doanmonhoc.R;
import com.example.doanmonhoc.activity.ProductManagement.ProductManagementActivity;
import com.example.doanmonhoc.adapter.ViewPagerAdapter;
import com.example.doanmonhoc.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationBarView;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding b;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        b = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

//        Button btnProduct = findViewById(R.id.btnProduct);
//        btnProduct.setOnClickListener(v -> {
//            Intent intent = new Intent(MainActivity.this, ProductManagementActivity.class);
//            startActivity(intent);
//        });
//
//        Button btnLogout = findViewById(R.id.btnLogout);
//        btnLogout.setOnClickListener(v -> logout());

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        b.viewPager.setAdapter(viewPagerAdapter);

        /**
         * Phương thức được gọi khi người dùng vuốt màn hình để chuyển giữa
         * các fragment trong ViewPager2. Ta cài đặt xử lý đểm đảm bảo rằng khi
         * chuyển qua fragment khác thì icon ở BottomNavigationBar,
         * sẽ được cập nhật đúng icon cho fragment đó.
         **/
//        b.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
//            @Override
//            public void onPageSelected(int position) {
//                super.onPageSelected(position);
//                updateButtonNavigationView(position);
//            }
//        });

        /**
         * Interface setOnNavigationItemSelectedListener không khả dụng.
         * Interface setOnItemSelectedListener được sử dụng thay thế.
         *
         **/
        b.bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            /**
             * Kịch bản sử dụng:
             * Người dùng mở ứng dụng lên, và `HomepageFragment` (position = 0)
             * được mở theo mặc định. Icon của HomepageFragment được gán là đang được chọn.
             *
             * Người dùng bấm nút Homepage (trang Quản Lí), ứng dụng bắt sự kiện nhấn và
             * thực thi phương thức `onNavigationItemSelected` để kiểm tra điều kiện:
             * `item.getItemId() == R.id.navigation_button_homepage`.
             * Sau đó thực hiện gán cho viewPager với HomePage fragment để hiển thị.
             *
             * @param item đối tượng trên bottomNavigationBar đã bấm
             * @return true nếu đã được xử lý, false nếu không tìm thấy id nào trùng.
             **/
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Do sự thay đổi của Android, không còn có thể sử dụng id của
                // các resource trong switch-case.
                // Không chuyển qua sử dụng if hoặc sử dụng hashmap để lưu id
                // của các resource.
                Map<Integer, Integer> menuItems = new HashMap<>();

                menuItems.put(R.id.navigation_button_homepage, 0);
                menuItems.put(R.id.navigation_button_sell, 1);
                menuItems.put(R.id.navigation_button_product, 2);
                menuItems.put(R.id.navigation_button_stock, 3);
                menuItems.put(R.id.navigation_icon_addition, 4);

                Integer position = menuItems.get(item.getItemId());
                if (position == null) {
                    return false;
                }

                switch (position) {
                    case 0:
                        b.viewPager.setCurrentItem(0);
                        break;
                    case 1:
                        b.viewPager.setCurrentItem(1);
                        break;
                    case 2:
                        startActivity(new Intent(getApplicationContext(), ProductManagementActivity.class));
                        break;
                    case 3:
                        b.viewPager.setCurrentItem(3);
                        break;
                    case 4:
                        b.viewPager.setCurrentItem(4);
                        break;
                }
                return true;
            }
        });
    }

    private void updateButtonNavigationView(int position) {
        int correspondingItemId;
        switch (position) {
            default:
            case 0:
                correspondingItemId = R.id.navigation_button_homepage;
                break;
            case 1:
                correspondingItemId = R.id.navigation_button_sell;
                break;
            case 2:
                correspondingItemId = R.id.navigation_button_product;
                break;
            case 3:
                correspondingItemId = R.id.navigation_button_stock;
                break;
            case 4:
                correspondingItemId = R.id.navigation_icon_addition;
                break;
        }
        b.bottomNavigationView
                .getMenu()                          // Trả về instance của menu gán với navigationBar.
                .findItem(correspondingItemId)      // Trả về item theo id trong correspondingItemId.
                .setChecked(true);                  // Gắn cờ cho item là đang được chọn.
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
