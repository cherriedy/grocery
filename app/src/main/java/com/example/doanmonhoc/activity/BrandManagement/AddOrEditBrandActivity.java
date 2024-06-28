package com.example.doanmonhoc.activity.BrandManagement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.doanmonhoc.R;
import com.example.doanmonhoc.contract.BrandManagement.AddOrEditBrandContract;
import com.example.doanmonhoc.databinding.ActivityAddOrEditBrandBinding;
import com.example.doanmonhoc.model.Brand;
import com.example.doanmonhoc.presenter.BrandManagement.AddOrEditBrandPresenter;
import com.example.doanmonhoc.utils.IntentManager;
import com.example.doanmonhoc.utils.LoadingDialog;
import com.example.doanmonhoc.utils.TextUtils;
import com.example.doanmonhoc.utils.validation.TextWatcherValidation;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;

public class AddOrEditBrandActivity extends AppCompatActivity implements AddOrEditBrandContract.View {
    private static final String TAG = "AddOrEditBrandActivity";

    private ActivityAddOrEditBrandBinding binding;
    private AddOrEditBrandPresenter presenter;
    private LoadingDialog loadingDialog;
    private Brand mExtraBrand;
    private BottomSheetDialog mConfirmDeletionDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityAddOrEditBrandBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.primaryColor));

        onFocusHeader();

        loadingDialog = new LoadingDialog(this);
        presenter = new AddOrEditBrandPresenter(this);
        mConfirmDeletionDialog = new BottomSheetDialog(this);

        mConfirmDeletionDialog.setCancelable(false);

        handleIntent(getIntent());

        validateBrandName();

        binding.actionBack.setOnClickListener(v -> onBackPressed());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void createBrand() {
        String textProductBrandName = TextUtils.getString(binding.textProductBrandName);
        if (!isBrandInputEmpty()) {
            loadingDialog.show();
            presenter.handleCreateBrand(new Brand(textProductBrandName));
        } else {
            Toast.makeText(this, "Không để trống thông tin", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void createBrandSuccessfully() {
        loadingDialog.hide();
        Toast.makeText(this, "Tạo nhãn hàng thành công", Toast.LENGTH_SHORT).show();
        setResult(Activity.RESULT_OK, new Intent());
        finish();
    }

    @Override
    public void createBrandFail() {
        loadingDialog.hide();
        Toast.makeText(this, "Tạo nhãn hàng thất bại", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getExtraBrandFail() {
        loadingDialog.hide();
        Toast.makeText(this, "Xảy ra lỗi nhận thông tin!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getExtraBrandSuccessfully(Brand brand) {
        mExtraBrand = brand;
        String brandName = brand.getBrandName();
        if (TextUtils.isValidString(brandName)) {
            binding.textProductBrandName.setText(brandName);
        } else {
            Log.e(TAG, "getExtraBrandSuccessfully - brandName: " + "Không có dữ liệu");
        }
    }

    @Override
    public void deleteBrand() {
        loadingDialog.show();
        presenter.handleDeleteBrand(mExtraBrand.getId());
    }

    @Override
    public void deleteBrandSuccessfully() {
        loadingDialog.hide();
        Toast.makeText(this, "Xóa sản phẩm thành công", Toast.LENGTH_SHORT).show();
        setResult(Activity.RESULT_OK, new Intent());
        finish();
    }

    @Override
    public void deleteBrandFail() {
        loadingDialog.hide();
        Toast.makeText(this, "Xóa  sản phẩm thất bại", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateBrand() {
        if (!isBrandInputEmpty()) {
            loadingDialog.show();
            String brandNameText = TextUtils.getString(binding.textProductBrandName);
            Brand brand = new Brand();
            brand.setBrandName(brandNameText);
            presenter.handleUpdateBrand(mExtraBrand.getId(), brand);
        } else {
            Toast.makeText(this, "Không để trống thông tin", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onUpdateBrandSuccess() {
        loadingDialog.hide();
        Toast.makeText(this, "Cập nhật nhãn hàng thành công", Toast.LENGTH_SHORT).show();
        setResult(Activity.RESULT_OK, new Intent());
        finish();
    }

    @Override
    public void onUpdateBrandFail() {
        loadingDialog.hide();
        Toast.makeText(this, "Cập nhật nhãn hàng thất bại", Toast.LENGTH_SHORT).show();
    }

    private void handleIntent(Intent intent) {
        String intentMode = intent.getStringExtra(IntentManager.ModeParams.EXTRA_MODE);
        if (TextUtils.isValidString(intentMode)) {
            switch (intentMode) {
                case IntentManager.ModeParams.EXTRA_MODE_CREATE:
                    binding.buttonFinish.setOnClickListener(v -> createBrand());
                    break;

                case IntentManager.ModeParams.EXTRA_MODE_EDIT:
                    binding.textActionBarHeader.setText("Cập nhật thương hiệu");
                    binding.buttonDelete.setVisibility(View.VISIBLE);
                    binding.viewDividerButton.setVisibility(View.VISIBLE);
                    binding.buttonDelete.setOnClickListener(v -> {
                        View dialogLayout = getLayoutInflater().inflate(R.layout.dialog_confirm_deletion, null);
                        TextView titleDialog = dialogLayout.findViewById(R.id.title_dialog);
                        TextView textNotification = dialogLayout.findViewById(R.id.text_notification);
                        MaterialButton buttonCancel = dialogLayout.findViewById(R.id.button_cancel);
                        MaterialButton buttonApprove = dialogLayout.findViewById(R.id.button_approve);

                        titleDialog.setText(R.string.delete_brand_dialog_title);
                        textNotification.setText(getString(R.string.msg_delete_brand));
                        buttonCancel.setOnClickListener(v1 -> mConfirmDeletionDialog.dismiss());
                        buttonApprove.setOnClickListener(v2 -> deleteBrand());

                        mConfirmDeletionDialog.setContentView(dialogLayout);
                        mConfirmDeletionDialog.show();

                    });
                    binding.buttonFinish.setOnClickListener(v -> updateBrand());
                    presenter.getExtraBrand(intent);
                    break;
            }
        }
    }

    private void onFocusHeader() {
        TextUtils.onFocusHeader(this, binding.textHeadingProductBrandDes, binding.textProductBrandDes);
        TextUtils.onFocusHeader(this, binding.textHeadingProductBrandName, binding.textProductBrandName);
    }

    private void validateBrandName() {
        binding.textProductBrandName.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                isBrandInputEmpty();
            }
        });

        binding.textProductBrandName.addTextChangedListener(new TextWatcherValidation() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count != before) {
                    isBrandInputEmpty();
                }
            }
        });
    }

    private boolean isBrandInputEmpty() {
        if (TextUtils.getString(binding.textProductBrandName).isEmpty()) {
            binding.textLayoutProductBrandName.setError(getString(R.string.msg_text_product_brand_requirement));
            return true;
        }
        binding.textLayoutProductBrandName.setError(null);
        return false;
    }
}
