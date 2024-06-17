package com.example.doanmonhoc.activity.ProductBrandManagement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.doanmonhoc.R;
import com.example.doanmonhoc.contract.ProductBrandManagement.AddProductBrandContract;
import com.example.doanmonhoc.databinding.ActivityAddProductBrandBinding;
import com.example.doanmonhoc.model.Brand;
import com.example.doanmonhoc.presenter.ProductBrandManagement.AddProductBrandPresenter;
import com.example.doanmonhoc.utils.TextUtils;
import com.example.doanmonhoc.utils.validation.TextWatcherValidation;

public class AddProductBrandActivity extends AppCompatActivity implements AddProductBrandContract.View {
    private ActivityAddProductBrandBinding binding;
    private AddProductBrandPresenter addProductBrandPresenter;
    private Intent resultIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityAddProductBrandBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.primaryColor));

        resultIntent = new Intent();
        addProductBrandPresenter = new AddProductBrandPresenter(AddProductBrandActivity.this);

        TextUtils.onFocusHeader(this, binding.textHeadingProductBrandDes, binding.textProductBrandDes);
        TextUtils.onFocusHeader(this, binding.textHeadingProductBrandName, binding.textProductBrandName);

        validateBrandName();
        binding.actionBack.setOnClickListener(v -> onBackPressed());
        binding.actionFinish.setOnClickListener(v -> {
            if (!isBrandInputEmpty()) {
                createProductBrand();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void createProductBrand() {
        String textProductBrandName = TextUtils.getString(binding.textProductBrandName);
        if (!textProductBrandName.isEmpty()) {
            Brand brand = new Brand(textProductBrandName);
            addProductBrandPresenter.handleCreateProductBrand(brand);
        } else {
            Toast.makeText(this, "Không để trống thông tin", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void createProductBrandSuccessfully() {
        Toast.makeText(this, "Tạo nhãn hàng thành công", Toast.LENGTH_SHORT).show();
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public void createProductBrandFail() {
        Toast.makeText(this, "Tạo nhãn hàng thất bại", Toast.LENGTH_SHORT).show();
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
