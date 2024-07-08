package com.example.doanmonhoc.activity.AccountManagement;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.doanmonhoc.R;
import com.example.doanmonhoc.api.CloudinaryService;
import com.example.doanmonhoc.api.KiotApiService;
import com.example.doanmonhoc.model.CloudinaryUploadResponse;
import com.example.doanmonhoc.model.Staff;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

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

public class EditAccountActivity extends AppCompatActivity {
    private Uri selectedImageUri;
    private EditText txtName, txtDob, txtGender, txtAddress, txtEmail, txtPhone;
    private TextView txtUsername, txtMaNV;
    private Button btnBack, btnSave, btnImage;
    private Staff staff;
    private RadioButton radioButtonMale, radioButtonFemale;
    private ShapeableImageView staffImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account_edit);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));

        txtMaNV = findViewById(R.id.txtMaNV);
        txtName = findViewById(R.id.txtName);
        txtDob = findViewById(R.id.txtDob);
        radioButtonMale = findViewById(R.id.radioButtonMale);
        radioButtonFemale = findViewById(R.id.radioButtonFemale);
        txtAddress = findViewById(R.id.txtAddress);
        txtEmail = findViewById(R.id.txtEmail);
        txtPhone = findViewById(R.id.txtPhone);
        staffImage = findViewById(R.id.staffImage);
        txtUsername = findViewById(R.id.txtusername);
        btnBack = findViewById(R.id.btnCancel);
        btnImage = findViewById(R.id.btnImage);
        btnSave = findViewById(R.id.btnSave);

        loadAccountDetail();

        txtDob.setOnClickListener(v -> {
            // Lấy ngày hiện tại để làm mặc định cho DatePicker
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            // Tạo DatePickerDialog
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    EditAccountActivity.this,
                    (view, year1, month1, dayOfMonth) -> {
                        // Hiển thị ngày đã chọn lên EditText
                        String selectedDate = year1 + "-" + (month1 + 1) + "-" + dayOfMonth;
                        txtDob.setText(selectedDate);
                    },
                    year, month, day) {
            };
            datePickerDialog.show();

        });

        btnBack.setOnClickListener(v -> finish());
        btnImage.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            activityResultLauncher.launch(intent);
        });
        btnSave.setOnClickListener(v -> updateStaff());
    }
    private void loadAccountDetail() {
        SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        long staffId = sharedPreferences.getLong("id", -1);

        if (staffId == -1) {
            Toast.makeText(this, "Không tìm thấy Staff ID trong SharedPreferences", Toast.LENGTH_SHORT).show();
            return;
        }

        KiotApiService.apiService.getStaffById(staffId).enqueue(new Callback<Staff>() {
            @Override
            public void onResponse(Call<Staff> call, Response<Staff> response) {
                if (response.isSuccessful() && response.body() != null) {
                    staff = response.body();
                    txtMaNV.setText(staff.getStaffKey());
                    txtName.setText(staff.getStaffName());

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    txtDob.setText(dateFormat.format(staff.getStaffDob()));

                    txtDob.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(staff.getStaffDob()));
                    if (staff.getStaffGender() == 1) {
                        radioButtonMale.setChecked(true);
                    } else {
                        radioButtonFemale.setChecked(true);
                    }
                    txtAddress.setText(staff.getAddress());
                    txtEmail.setText(staff.getStaffEmail());
                    txtPhone.setText(staff.getStaffPhone());
                    txtUsername.setText(staff.getUsername());

                    // Load staff image if available
                    if (staff.getStaffImage() != null && !staff.getStaffImage().isEmpty()) {
                        Picasso.get().load(staff.getStaffImage()).into(staffImage);
                    }
                } else {
                    Toast.makeText(EditAccountActivity.this, "Failed to get staff info", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Staff> call, Throwable t) {
                Toast.makeText(EditAccountActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    // Khởi tạo ActivityResultLauncher
    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getData() != null) {
                    selectedImageUri = result.getData().getData();
                    staffImage.setImageURI(selectedImageUri);
                }
            });

    private void updateStaff() {
        String newName = txtName.getText().toString().trim();
        String newDob = txtDob.getText().toString();
        String newAddress = txtAddress.getText().toString();
        String newEmail = txtEmail.getText().toString();
        String newPhone = txtPhone.getText().toString();
        RadioGroup radioGroupGender = findViewById(R.id.radioGroupGender);
        int selectedRadioButtonId = radioGroupGender.getCheckedRadioButtonId();

        int newGender = 1;
        RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
        if (selectedRadioButton.getId() == R.id.radioButtonFemale) {
            newGender = 0;
        }


        if (TextUtils.isEmpty(newName) || TextUtils.isEmpty(newDob) || TextUtils.isEmpty(newAddress) || TextUtils.isEmpty(newEmail) || TextUtils.isEmpty(newPhone)) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        staff.setStaffName(newName);
        staff.setStaffGender((byte) newGender);
        staff.setStaffEmail(newEmail);
        staff.setAddress(newAddress);
        staff.setStaffPhone(newPhone);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        try {
            Date date = sdf.parse(newDob);
            staff.setStaffDob(date);
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(this, "Định dạng ngày sinh không hợp lệ. Vui lòng nhập lại.", Toast.LENGTH_SHORT).show();
            return;
        }
        // Check selected image
        if (selectedImageUri != null) {
            try {
                InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                byte[] imageBytes = IOUtils.toByteArray(inputStream);
                saveToCloudinary(imageBytes);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(EditAccountActivity.this, "Failed to read image file", Toast.LENGTH_SHORT).show();
            }
        }else{
            saveToDatabase(staff);
        }
    }
    private void saveToCloudinary(byte[] imageBytes) {
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
                        Toast.makeText(EditAccountActivity.this, "Upload thành công: " + imageUrl, Toast.LENGTH_SHORT).show();
                        staff.setStaffImage(imageUrl); // Save image URL to staff object
                        saveToDatabase(staff);
                    } else {
                        Toast.makeText(EditAccountActivity.this, "Upload thất bại: Response body is empty", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(EditAccountActivity.this, "Upload thất bại: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CloudinaryUploadResponse> call, Throwable t) {
                Toast.makeText(EditAccountActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void saveToDatabase(Staff staff) {
        KiotApiService.apiService.updateStaff(staff.getId(), staff).enqueue(new Callback<Staff>() {
            @Override
            public void onResponse(Call<Staff> call, Response<Staff> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EditAccountActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(EditAccountActivity.this, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Staff> call, Throwable throwable) {
                Toast.makeText(EditAccountActivity.this, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}