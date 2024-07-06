package com.example.doanmonhoc.activity.StaffManagement;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.doanmonhoc.R;
import com.example.doanmonhoc.api.CloudinaryService;
import com.example.doanmonhoc.api.KiotApiService;
import com.example.doanmonhoc.model.CloudinaryUploadResponse;
import com.example.doanmonhoc.model.Staff;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddStaffActivity extends AppCompatActivity {
    private EditText txtName, txtDob, txtPhone, txtEmail, txtAddress, txtusername, txtpassword;
    private RadioGroup radioGroupGender;
    private Button btnSave, btnSelectImage;
    private ImageButton btnCancel;
    private RadioButton radioButtonMale, radioButtonFemale;

    private Uri selectedImageUri;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_add);

        gson = new GsonBuilder().setDateFormat("MMM d, yyyy hh:mm:ss a").create();

        txtName = findViewById(R.id.txtName);
        txtDob = findViewById(R.id.txtDob);
        radioGroupGender = findViewById(R.id.radioGroupGender);
        radioButtonMale = findViewById(R.id.radioButtonMale);
        radioButtonFemale = findViewById(R.id.radioButtonFemale);
        txtPhone = findViewById(R.id.txtPhone);
        txtEmail = findViewById(R.id.txtEmail);
        txtAddress = findViewById(R.id.txtAddress);
        txtusername = findViewById(R.id.txtusername);
        txtpassword = findViewById(R.id.txtpassword);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
        btnSelectImage = findViewById(R.id.btnImage);

        txtDob.setOnClickListener(v -> showDatePickerDialog());
        btnSelectImage.setOnClickListener(v -> selectImage());

        btnSave.setOnClickListener(v -> addNewStaff());
        btnCancel.setOnClickListener(v -> finish());
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(AddStaffActivity.this, (view, year1, month1, dayOfMonth) -> {
            String selectedDate = year1 + "-" + (month1 + 1) + "-" + dayOfMonth;
            txtDob.setText(selectedDate);
        }, year, month, day);
        datePickerDialog.show();
    }

    private void addNewStaff() {
        String name = txtName.getText().toString().trim();
        String dob = txtDob.getText().toString().trim();
        String phone = txtPhone.getText().toString().trim();
        String email = txtEmail.getText().toString().trim();
        String address = txtAddress.getText().toString().trim();
        String username = txtusername.getText().toString().trim();
        String password = txtpassword.getText().toString().trim();
        int gender = radioGroupGender.getCheckedRadioButtonId() == R.id.radioButtonMale ? 1 : 0;

        if (name.isEmpty() || dob.isEmpty() || phone.isEmpty() || email.isEmpty() || address.isEmpty() || username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.PHONE.matcher(phone).matches() || phone.length() != 10) {
            Toast.makeText(this, "Số điện thoại không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if email already exists
        checkEmailExists(email, name, dob, phone, address, username, password, gender);
    }

    private void checkEmailExists(String email, String name, String dob, String phone, String address, String username, String password, int gender) {
        KiotApiService.apiService.getStaffByEmail(email).enqueue(new Callback<Staff>() {
            @Override
            public void onResponse(Call<Staff> call, Response<Staff> response) {
                if (response.isSuccessful()) {
                    Staff existingStaff = response.body();
                    if (existingStaff != null) {
                        Toast.makeText(AddStaffActivity.this, "Email đã tồn tại", Toast.LENGTH_SHORT).show();
                    } else {
                        // Email does not exist, proceed to save
                        Staff newStaff = new Staff();
                        newStaff.setStaffName(name);
                        newStaff.setStaffPhone(phone);
                        newStaff.setStaffEmail(email);
                        newStaff.setAddress(address);
                        newStaff.setUsername(username);
                        newStaff.setPassword(password);
                        newStaff.setStaffGender((byte) gender);

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        try {
                            Date date = sdf.parse(dob);
                            newStaff.setStaffDob(date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                            Toast.makeText(AddStaffActivity.this, "Định dạng ngày không hợp lệ", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (selectedImageUri != null) {
                            try {
                                InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                                byte[] imageBytes = IOUtils.toByteArray(inputStream);
                                saveToCloudinary(imageBytes, newStaff);
                            } catch (IOException e) {
                                e.printStackTrace();
                                Toast.makeText(AddStaffActivity.this, "Failed to read image file", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            saveToDatabase(newStaff);
                        }
                    }
                } else {
                    Toast.makeText(AddStaffActivity.this, "Lỗi khi kiểm tra email", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Staff> call, Throwable t) {
                Toast.makeText(AddStaffActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activityResultLauncher.launch(intent);
    }

    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    selectedImageUri = result.getData().getData();
                    // You can set the image preview here if needed
                }
            }
    );

    private void saveToCloudinary(byte[] imageBytes, Staff newStaff) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), imageBytes);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", "image.jpg", requestFile);
        RequestBody uploadPreset = RequestBody.create(MediaType.parse("text/plain"), "ml_default");

        CloudinaryService.apiService.uploadImage(body, uploadPreset).enqueue(new Callback<CloudinaryUploadResponse>() {
            @Override
            public void onResponse(Call<CloudinaryUploadResponse> call, Response<CloudinaryUploadResponse> response) {
                if (response.isSuccessful()) {
                    CloudinaryUploadResponse uploadResponse = response.body();
                    if (uploadResponse != null) {
                        String imageUrl = uploadResponse.getUrl();
                        newStaff.setStaffImage(imageUrl);
                        saveToDatabase(newStaff);
                    }
                } else {
                    Toast.makeText(AddStaffActivity.this, "Cập nhật ảnh thất bại", Toast.LENGTH_SHORT).show();
                    saveToDatabase(newStaff); // Continue saving without image
                }
            }

            @Override
            public void onFailure(Call<CloudinaryUploadResponse> call, Throwable t) {
                Toast.makeText(AddStaffActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                saveToDatabase(newStaff); // Continue saving without image
            }
        });
    }

    private void saveToDatabase(Staff newStaff) {
        KiotApiService.apiService.createStaff(newStaff).enqueue(new Callback<Staff>() {
            @Override
            public void onResponse(Call<Staff> call, Response<Staff> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AddStaffActivity.this, "Thêm nhân viên thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AddStaffActivity.this, "Thêm nhân viên thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Staff> call, Throwable t) {
                Toast.makeText(AddStaffActivity.this, "Đã xảy ra lỗi", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
