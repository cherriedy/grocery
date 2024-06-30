package com.example.doanmonhoc.activity.Auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.doanmonhoc.R;
import com.example.doanmonhoc.api.KiotApiService;
import com.example.doanmonhoc.model.Staff;

import retrofit2.Callback;

public class ChangePasswordActivity extends AppCompatActivity {
    private EditText editNewPass, editNewPass1;
    private Button btnChangePass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);

        editNewPass = findViewById(R.id.editNewPass);
        editNewPass1 = findViewById(R.id.editNewPass1);

        btnChangePass = findViewById(R.id.btnChangePass);
        btnChangePass.setOnClickListener(v -> {
            String newPass = editNewPass.getText().toString();
            String newPass1 = editNewPass1.getText().toString();

            if(newPass.equals(newPass1)){
                updatePass(newPass1);
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                Toast.makeText(this, "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                finish();
            }else {
                Toast.makeText(this, "Nhập lại mật khẩu không đúng", Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void updatePass(String newPass1){
        Staff password = new Staff(newPass1);
//        KiotApiService.apiService.updateStaff(password).enqueue(new Callback<Staff>() {
//            @Override
//            public void onResponse(Call<List<GoodsReceivedNote>> call, Response<List<GoodsReceivedNote>> response) {
//                if (response.isSuccessful()) {
//                    List<GoodsReceivedNote> list = response.body();
//                    if (list != null && !list.isEmpty()) {
//                        importList.clear();
//                        importList.addAll(list);
//                        filteredImportList.clear();
//                        filteredImportList.addAll(list);
//                        adapter = new ImportManagementAdapter(ImportManagementActivity.this, filteredImportList);
//                        listView.setAdapter(adapter);
//                    } else {
//                        Toast.makeText(ImportManagementActivity.this, "Không tìm thấy sản phẩm nào!", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Toast.makeText(ImportManagementActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<GoodsReceivedNote>> call, Throwable t) {
//                Toast.makeText(ImportManagementActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
//            }
//        });
    }
}
