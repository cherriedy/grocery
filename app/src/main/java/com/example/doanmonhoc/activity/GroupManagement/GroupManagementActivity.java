package com.example.doanmonhoc.activity.GroupManagement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.doanmonhoc.R;
import com.example.doanmonhoc.adapter.GroupListAdapter;
import com.example.doanmonhoc.contract.GroupManagement.GroupManagementContract;
import com.example.doanmonhoc.databinding.ActivityGroupManagementBinding;
import com.example.doanmonhoc.model.ProductGroup;
import com.example.doanmonhoc.presenter.GroupManagement.GroupManagementPresenter;
import com.example.doanmonhoc.utils.IntentManager;
import com.example.doanmonhoc.utils.LoadingDialog;

import java.util.List;

public class GroupManagementActivity extends AppCompatActivity implements GroupManagementContract.View, GroupListAdapter.OnItemClickListener {
    private static final String TAG = GroupManagementActivity.class.getSimpleName();

    private ActivityGroupManagementBinding binding;
    private LoadingDialog mLoadingDialog;
    private boolean isMenuOpen;
    private Animation mAnimFromBottomFab;
    private Animation mAnimToBottomFab;
    private Animation mAnimRotateClockWise;
    private Animation mAnimRotateAntiClockWise;
    private GroupManagementPresenter mPresenter;
    private GroupListAdapter mGroupListAdapter;
    private List<ProductGroup> mGroupList;
    private ActivityResultLauncher<Intent> mGetResultOkLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityGroupManagementBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.primaryColor));

        Log.d(TAG, "onCreate: ");

        mGetResultOkLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), o -> {
                    if (o.getResultCode() == Activity.RESULT_OK) {
                        mGroupListAdapter.setData(null);
                        binding.progressBar.setVisibility(View.VISIBLE);
                        mPresenter.fetchGroupList();
                    }
                }
        );

        initializeAnimation();
        onExpandMenuClick();
        onAddOneClick();

        mPresenter = new GroupManagementPresenter(this);
        mLoadingDialog = new LoadingDialog(this);
        mGroupListAdapter = new GroupListAdapter(this);

        mGroupListAdapter.setOnClickListener(this);

        binding.listGroup.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        );

        mPresenter.fetchGroupList();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onGroupListFetchSuccess(List<ProductGroup> groupList) {
        binding.progressBar.setVisibility(View.INVISIBLE);
        mGroupList = groupList;

        if (mGroupList == null) {
            Toast.makeText(this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
            return;
        }

        mGroupListAdapter.setData(groupList);
        binding.listGroup.setAdapter(mGroupListAdapter);
    }

    @Override
    public void onGroupListFetchFail() {
        mLoadingDialog.hide();
        Toast.makeText(this, "Lỗi truy vấn dữ liệu", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void createGroup() {
        Intent intent = new Intent(this, AddOrEditGroupActivity.class);
        intent.putExtra(
                IntentManager.ModeParams.EXTRA_MODE,
                IntentManager.ModeParams.EXTRA_MODE_CREATE
        );
        mGetResultOkLauncher.launch(intent);
    }

    @Override
    public void onItemClick(ProductGroup group) {
        Intent intent = new Intent(this, AddOrEditGroupActivity.class);
        intent.putExtra(
                IntentManager.ModeParams.EXTRA_MODE,
                IntentManager.ModeParams.EXTRA_MODE_EDIT
        );
        intent.putExtra(IntentManager.ExtraParams.EXTRA_GROUP, group);
        mGetResultOkLauncher.launch(intent);
    }

    private void initializeAnimation() {
        mAnimFromBottomFab = AnimationUtils.loadAnimation(this, R.anim.from_bottom_fab);
        mAnimToBottomFab = AnimationUtils.loadAnimation(this, R.anim.to_bottom_fab);
        mAnimRotateClockWise = AnimationUtils.loadAnimation(this, R.anim.rotate_clock_wise);
        mAnimRotateAntiClockWise = AnimationUtils.loadAnimation(this, R.anim.rotate_anti_clock_wise);
    }

    private void onExpandMenuClick() {
        binding.fabExpandMenu.setOnClickListener(v -> {
            if (!isMenuOpen) {
                openMenu();
            } else {
                closeMenu();
            }
        });
    }

    private void openMenu() {
        isMenuOpen = true;
        binding.fabExpandMenu.startAnimation(mAnimRotateAntiClockWise);
        binding.fabAddOne.startAnimation(mAnimToBottomFab);
        binding.textAddOne.startAnimation(mAnimToBottomFab);
    }

    private void closeMenu() {
        isMenuOpen = false;
        binding.fabExpandMenu.startAnimation(mAnimRotateClockWise);
        binding.fabAddOne.startAnimation(mAnimFromBottomFab);
        binding.textAddOne.startAnimation(mAnimFromBottomFab);
    }

    private void onAddOneClick() {
        binding.fabAddOne.setOnClickListener(v -> createGroup());
    }
}