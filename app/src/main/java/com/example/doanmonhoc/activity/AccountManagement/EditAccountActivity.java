package com.example.doanmonhoc.activity.AccountManagement;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.doanmonhoc.R;
import com.example.doanmonhoc.api.KiotApiService;
import com.example.doanmonhoc.model.Staff;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditAccountActivity extends AppCompatActivity {

    private EditText txtName, txtDob, txtGender, txtAddress, txtEmail, txtPhone, txtpassword;
    private TextView txtusername;
    private Button btnBack, btnSave;
    private Staff staff;
    private Gson gson;
    private RadioButton radioButtonMale, radioButtonFemale;
    private ShapeableImageView staffImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account_edit);

        gson = new GsonBuilder().setDateFormat("MMM d, yyyy hh:mm:ss a").create();

        String staffJson = getIntent().getStringExtra("staff");
        staff = gson.fromJson(staffJson, Staff.class);

        txtName = findViewById(R.id.txtName);
        txtDob = findViewById(R.id.txtDob);
        radioButtonMale = findViewById(R.id.radioButtonMale);
        radioButtonFemale = findViewById(R.id.radioButtonFemale);
        txtAddress = findViewById(R.id.txtAddress);
        txtEmail = findViewById(R.id.txtEmail);
        txtPhone = findViewById(R.id.txtPhone);
        staffImage = findViewById(R.id.staffImage);
        txtusername = findViewById(R.id.txtusername);
        txtpassword = findViewById(R.id.txtPassword);

        // setText
        txtName.setText(staff.getStaffName());
        txtDob.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(staff.getStaffDob()));
        if (staff.getStaffGender() == 1) {
            radioButtonMale.setChecked(true);
        } else {
            radioButtonFemale.setChecked(true);
        }
        txtAddress.setText(staff.getAddress());
        txtEmail.setText(staff.getStaffEmail());
        txtPhone.setText(staff.getStaffPhone());
        txtusername.setText(staff.getUsername());
        txtpassword.setText(staff.getPassword());

        if (staff.getStaffImage() != null && !staff.getStaffImage().isEmpty()) {
            int resID = getResources().getIdentifier(staff.getStaffImage(), "drawable", getPackageName());
            staffImage.setImageResource(resID);
            staffImage.setTag(staff.getStaffImage()); // Lưu tên tài nguyên vào tag
        }


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
                @Override
                protected void onCreate(Bundle savedInstanceState) {
                    super.onCreate(savedInstanceState);
                    // Lấy nút "OK" và "Cancel" và thay đổi màu sắc của chúng
                    int okButtonId = getResources().getIdentifier("android:id/button1", null, null);
                    Button okButton = findViewById(okButtonId);
                    okButton.setTextColor(Color.BLACK);

                    int cancelButtonId = getResources().getIdentifier("android:id/button2", null, null);
                    Button cancelButton = findViewById(cancelButtonId);
                    cancelButton.setTextColor(Color.BLACK);
                }
            };
            datePickerDialog.show();

        });


        btnBack = findViewById(R.id.btnCancel);
        btnBack.setOnClickListener(v -> finish());

        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(v -> updateStaff());
    }

    private void updateStaff() {
        String newName = txtName.getText().toString().trim();
        String newDob = txtDob.getText().toString();
        String newAddress = txtAddress.getText().toString();
        String newEmail = txtEmail.getText().toString();
        String newPhone = txtPhone.getText().toString();
        String newPassword = txtpassword.getText().toString();
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
        staff.setPassword(newPassword);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        try {
            Date date = sdf.parse(newDob);
            staff.setStaffDob(date);
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(this, "Định dạng ngày sinh không hợp lệ. Vui lòng nhập lại.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (staffImage.getTag() != null) {
            staff.setStaffImage(staffImage.getTag().toString());
        }

        KiotApiService.apiService.updateStaff(staff.getId(), staff).enqueue(new Callback<Staff>() {
            @Override
            public void onResponse(Call<Staff> call, Response<Staff> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EditAccountActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(EditAccountActivity.this, "Cập nhật thất bạiiiiiiiiiiiii!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Staff> call, Throwable throwable) {
                Toast.makeText(EditAccountActivity.this, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
