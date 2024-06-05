package com.example.doanmonhoc.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.doanmonhoc.R;
import com.example.doanmonhoc.RetrofitClient;
import com.example.doanmonhoc.api.ApiService;
import com.example.doanmonhoc.model.Account;
import com.example.doanmonhoc.model.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText edtUsername = findViewById(R.id.edtUsername);
        EditText edtPassword = findViewById(R.id.edtPassword);
        Button btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> {
            String username = edtUsername.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();

            if (!username.isEmpty() && !password.isEmpty()) {
                login(username, password);
            } else {
                Toast.makeText(LoginActivity.this, "Hãy nhập username và mật khẩu", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void login(String username, String password) {
        ApiService apiService = RetrofitClient.getApiService(this);
        Account account = new Account(username, password);
        Call<LoginResponse> call = apiService.loginUser(account);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() ) {
                    LoginResponse loginResponse = response.body();
                    if (loginResponse != null) {

                        String message = loginResponse.getMessage();
                        long Roleid = loginResponse.getRoleid();
                        long id = loginResponse.getId();

                        // Lưu vào SharedPreferences
                        SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putLong("id", id);
                        editor.putLong("Roleid", Roleid);
                        editor.apply();

                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công! " + message, Toast.LENGTH_SHORT).show();
                        Intent intent;
                        if (Roleid == 1) {
                            intent = new Intent(LoginActivity.this, MainActivity.class);
                        } else {
                            intent = new Intent(LoginActivity.this, MainActivityForStaff.class);
                        }
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Sai username hoặc mật khẩu ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
