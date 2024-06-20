package com.example.doanmonhoc.activity.GroupManagement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import com.example.doanmonhoc.contract.GroupManagement.AddOrEditGroupContract;
import com.example.doanmonhoc.databinding.ActivityAddOrEditGroupBinding;
import com.example.doanmonhoc.model.ProductGroup;
import com.example.doanmonhoc.presenter.GroupManagement.AddOrEditGroupPresenter;
import com.example.doanmonhoc.utils.IntentManager;
import com.example.doanmonhoc.utils.LoadingDialog;
import com.example.doanmonhoc.utils.TextUtils;
import com.example.doanmonhoc.utils.validation.TextWatcherValidation;
import com.example.doanmonhoc.utils.validation.ValidationUtils;

public class AddOrEditGroupActivity extends AppCompatActivity implements AddOrEditGroupContract.View {
    private static final String TAG = AddOrEditGroupActivity.class.getSimpleName();

    private ActivityAddOrEditGroupBinding binding;
    private AddOrEditGroupPresenter mPresenter;
    private ProductGroup mExtraProductGroup;
    private LoadingDialog mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityAddOrEditGroupBinding.inflate(getLayoutInflater());
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
        callGroupValidation();

        mPresenter = new AddOrEditGroupPresenter(this);
        mLoadingDialog = new LoadingDialog(this);

        handleIntent(getIntent());
    }

    @Override
    public void onExtraGroupProcessingSuccess(ProductGroup group) {
        mExtraProductGroup = group;
        String groupName = group.getProductGroupName();

        if (TextUtils.isValidString(groupName)) {
            binding.textProductGroupName.setText(groupName);
        }
    }

    @Override
    public void onExtraGroupProcessingFail() {
        binding.textProductGroupName.setHint(null);
        binding.textHeadingProductGroupDes.setHint(null);
        Toast.makeText(this, getString(R.string.msg_error_retrived_data), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void createGroup() {
        ProductGroup group = new ProductGroup();
        String groupName = TextUtils.getString(binding.textProductGroupName);

        group.setProductGroupName(groupName);

        if (!handleGroupNameValidation()) {
            Toast.makeText(this, getString(R.string.msg_fill_all_requierd_fields), Toast.LENGTH_SHORT).show();
        } else {
            mLoadingDialog.show();
            mPresenter.handleCreateGroup(group);
        }
    }

    @Override
    public void onGroupCreationSuccess() {
        mLoadingDialog.hide();
        Toast.makeText(this, "Tạo nhóm sản phẩm thành công", Toast.LENGTH_SHORT).show();
        setResult(Activity.RESULT_OK, new Intent());
        finish();
    }

    @Override
    public void onGroupCreationFail() {
        mLoadingDialog.hide();
        Toast.makeText(this, "Tạo nhóm sản phẩm thất bại", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateGroup() {
        String groupName = TextUtils.getString(binding.)
    }

    @Override
    public void onGroupUpdateSuccess() {
        mLoadingDialog.hide();
        Toast.makeText(this, "Cập nhật nhóm sản phẩm thành công", Toast.LENGTH_SHORT).show();
        setResult(Activity.RESULT_OK, new Intent());
        finish();
    }

    @Override
    public void onGroupUpdateFail() {
        mLoadingDialog.hide();
        Toast.makeText(this, "Cập nhật nhóm sản phẩm thất bại", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void deleteGroup() {
        mLoadingDialog.show();
        mPresenter.handleDeleteGroup(mExtraProductGroup.getId());
    }

    @Override
    public void onGroupDeleteSuccess() {
        mLoadingDialog.hide();
        Toast.makeText(this, "Xóa sản phẩm thành công", Toast.LENGTH_SHORT).show();
        setResult(Activity.RESULT_OK, new Intent());
        finish();
    }

    @Override
    public void onGroupDeleteFail() {
        mLoadingDialog.hide();
        Toast.makeText(this, "Xóa sản phẩm thất bại", Toast.LENGTH_SHORT).show();
    }

    private void handleIntent(Intent intent) {
        String intentMode = intent.getStringExtra(IntentManager.ModeParams.EXTRA_MODE);
        if (TextUtils.isValidString(intentMode)) {
            switch (intentMode) {
                case IntentManager.ModeParams.EXTRA_MODE_CREATE:
                    binding.buttonFinish.setOnClickListener(v -> createGroup());
                    break;
                case IntentManager.ModeParams.EXTRA_MODE_EDIT:
                    binding.textActionBarHeader.setText("Chi tiết nhóm sản phẩm");
                    binding.buttonDelete.setVisibility(View.VISIBLE);
                    binding.viewDividerButton.setVisibility(View.VISIBLE);
                    binding.buttonFinish.setOnClickListener(v -> updateGroup());
                    binding.buttonDelete.setOnClickListener(v -> deleteGroup());
                    mPresenter.handleExtraGroup(intent);
                    break;
            }
        }
    }

    private void groupNameValidation() {
        binding.textProductGroupName.addTextChangedListener(new TextWatcherValidation() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handleGroupNameValidation();
            }
        });

        binding.textProductGroupName.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                handleGroupNameValidation();
            }
        });
    }

    private boolean handleGroupNameValidation() {
        String groupName = TextUtils.getString(binding.textProductGroupName);
        ValidationUtils.ValidationResult validationResult
                = ValidationUtils.validateName(groupName);
        binding.textLayoutProductGroupName.setError(
                validationResult.getMessage()
        );
        return validationResult.isValid();
    }

    private void callGroupValidation() {
        groupNameValidation();
    }

    private void onFocusHeader() {
        TextUtils.onFocusHeader(this, binding.textHeadingProductGroupName, binding.textProductGroupName);
    }
}