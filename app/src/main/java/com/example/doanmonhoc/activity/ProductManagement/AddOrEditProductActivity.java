package com.example.doanmonhoc.activity.ProductManagement;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.doanmonhoc.R;
import com.example.doanmonhoc.adapter.BrandSpinnerAdapter;
import com.example.doanmonhoc.adapter.TypeSpinnerAdapter;
import com.example.doanmonhoc.contract.ProductManagement.AddOrEditProductContract;
import com.example.doanmonhoc.databinding.ActivityAddProductBinding;
import com.example.doanmonhoc.model.Brand;
import com.example.doanmonhoc.model.DetailedGoodsReceivedNote;
import com.example.doanmonhoc.model.GoodsReceivedNote;
import com.example.doanmonhoc.model.Product;
import com.example.doanmonhoc.model.ProductGroup;
import com.example.doanmonhoc.presenter.ProductManagament.AddOrEditProductPresenter;
import com.example.doanmonhoc.utils.IntentManager;
import com.example.doanmonhoc.utils.LoadingDialog;
import com.example.doanmonhoc.utils.NumberUtils;
import com.example.doanmonhoc.utils.PermissionManager;
import com.example.doanmonhoc.utils.PicassoHelper;
import com.example.doanmonhoc.utils.PrefsUtils;
import com.example.doanmonhoc.utils.TextUtils;
import com.example.doanmonhoc.utils.validation.TextWatcherValidation;
import com.example.doanmonhoc.utils.validation.ValidationUtils;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddOrEditProductActivity extends AppCompatActivity implements AddOrEditProductContract.View {
    public static final int RADIO_BUTTON_IN_STOCK = 0;
    public static final int RADIO_BUTTON_OUT_STOCK = 1;
    private static final String TAG = AddOrEditProductActivity.class.getSimpleName();

    private ActivityAddProductBinding binding;
    private String mIntentMode;
    private List<Brand> mBrandList;
    private List<ProductGroup> mTypeList;
    private AddOrEditProductPresenter mPresenter;
    private LoadingDialog mLoadingDialog;
    private Map<Integer, Integer> mRadioButton;
    private Product mExtraProduct;
    private BrandSpinnerAdapter mBrandAdapter;
    private TypeSpinnerAdapter mTypeAdapter;
    private BottomSheetDialog mConfirmDeletionDialog;
    private ActivityResultLauncher<Intent> mOpenGalleryLauncher;
    private ActivityResultLauncher<Uri> mTakePictureLauncher;
    private Uri mSelectedPictureUri;
    private SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityAddProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.primaryColor));

        mRadioButton = new HashMap<>();
        mLoadingDialog = new LoadingDialog(this);
        mBrandAdapter = new BrandSpinnerAdapter(this);
        mTypeAdapter = new TypeSpinnerAdapter(this);
        mPresenter = new AddOrEditProductPresenter(this);
        mConfirmDeletionDialog = new BottomSheetDialog(this);
        mSelectedPictureUri = PermissionManager.createUriForTakePicture(this);
        mPrefs = getSharedPreferences("myPrefs", MODE_PRIVATE);

        registerOpenGalleryLauncher();
        registerTakePictureLauncher();
        registerOnClickListener();
        registerProductValidation();
        onFocusHeader();

        mRadioButton.put(R.id.button_inStock, RADIO_BUTTON_IN_STOCK);
        mRadioButton.put(R.id.button_outStock, RADIO_BUTTON_OUT_STOCK);

        handleIntent(getIntent());
        handleFeatureByRole();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Lấy dữ liệu từ database hiển thị lên Spinner
        mPresenter.getBrandList();
        mPresenter.getProductGroupList();
    }

    @Override
    public void updateProduct() {
        String nameText = TextUtils.getString(binding.textProductName);
        String barcodeText = TextUtils.getString(binding.textProductBarcode);
        String descriptionText = TextUtils.getString(binding.textProductDescription);
        String noteText = TextUtils.getString(binding.textProductNote);
        String outPriceText = TextUtils.getString(binding.textProductOutPrice);
        String inPriceText = TextUtils.getString(binding.textProductInPrice);
        String discountText = TextUtils.getString(binding.textProductDiscount);

        float outPrice = NumberUtils.parseFloatOrDefault(outPriceText);
        float inPrice = NumberUtils.parseFloatOrDefault(inPriceText);
        float discount = NumberUtils.parseFloatOrDefault(discountText);

        long selectedBrandItemId = binding.spinnerBrand.getSelectedItemId();
        long selectedTypeItemId = binding.spinnerType.getSelectedItemId();
        long selectedBrand = mBrandList.get((int) selectedBrandItemId).getId();
        long selectedType = mTypeList.get((int) selectedTypeItemId).getId();

        Product product = new Product();
        product.setProductName(nameText);
        product.setOutPrice(outPrice);
        product.setProductNote(noteText);
        product.setDescription(descriptionText);
        product.setProductBrandId(selectedBrand);
        product.setProductGroupId(selectedType);

        product.setStatus((byte) 1);
        if (binding.buttonOutStock.isSelected()) {
            product.setStatus((byte) 0);
        } else {
            product.setStatus((byte) 1);
        }

        if (inPrice > 0.0f) {
            product.setInPrice(inPrice);
        }

        if (discount > 0.0f) {
            product.setDiscount(discount);
        }

        if (!(barcodeText.isEmpty())) {
            product.setProductBarcode(barcodeText);
        }

        if (!(noteText.isEmpty())) {
            product.setProductNote(noteText);
        }

        if (handleValidateProductName() && handleValidateOutPrice() && handleValidateDescription()) {
            mLoadingDialog.show();
            mPresenter.handleUpdateProduct(product);
        } else {
            Toast.makeText(this, getString(R.string.msg_fill_all_requierd_fields), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void createProduct() {
        SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        long staffId = sharedPreferences.getLong("id", -1);
        String nameText = TextUtils.getString(binding.textProductName);
        String barcodeText = TextUtils.getString(binding.textProductBarcode);
        String descriptionText = TextUtils.getString(binding.textProductDescription);
        String noteText = TextUtils.getString(binding.textProductNote);
        String outPriceText = TextUtils.getString(binding.textProductOutPrice);
        String inPriceText = TextUtils.getString(binding.textProductInPrice);
        String discountText = TextUtils.getString(binding.textProductDiscount);
        String quantityText = TextUtils.getString(binding.textProductQuantity);

        float outPrice = NumberUtils.parseFloatOrDefault(outPriceText);
        float inPrice = NumberUtils.parseFloatOrDefault(inPriceText);
        float discount = NumberUtils.parseFloatOrDefault(discountText);
        int quantity = NumberUtils.parseIntegerOrDefault(quantityText);

        long selectedBrandItemId = binding.spinnerBrand.getSelectedItemId();
        long selectedTypeItemId = binding.spinnerType.getSelectedItemId();
        long selectedBrand = mBrandList.get((int) selectedBrandItemId).getId();
        long selectedType = mTypeList.get((int) selectedTypeItemId).getId();

        Product product = new Product();
        product.setProductKey(mPresenter.generateLatestProductKey());
        product.setProductName(nameText);
        product.setOutPrice(outPrice);
        product.setProductNote(noteText);
        product.setDescription(descriptionText);
        product.setProductBrandId(selectedBrand);
        product.setProductGroupId(selectedType);
        product.setActualQuantity(quantity);

        GoodsReceivedNote goodsReceivedNote = new GoodsReceivedNote();
        goodsReceivedNote.setStaffid(staffId);

        DetailedGoodsReceivedNote detailedGoodsReceivedNote = new DetailedGoodsReceivedNote();
        detailedGoodsReceivedNote.setQuantity(quantity);
        detailedGoodsReceivedNote.setPrice(outPrice);

        product.setStatus((byte) 1);
        if (binding.buttonOutStock.isSelected()) {
            product.setStatus((byte) 0);
        } else {
            product.setStatus((byte) 1);
        }

        if (inPrice > 0.0f) {
            product.setInPrice(inPrice);
        }

        if (discount > 0.0f) {
            product.setDiscount(discount);
        }

        if (!(barcodeText.isEmpty())) {
            product.setProductBarcode(barcodeText);
        }

        if (!(noteText.isEmpty())) {
            product.setProductNote(noteText);
        }

        Map<String, Object> productCreationRequest = new HashMap<>();
        productCreationRequest.put("product", product);
        productCreationRequest.put("grn", goodsReceivedNote);
        productCreationRequest.put("dgrn", detailedGoodsReceivedNote);

        if (handleValidateProductName() && handleValidateOutPrice() && handleValidateDescription()) {
            mLoadingDialog.show();
            mPresenter.handleCreateProduct(productCreationRequest);
        } else {
            Toast.makeText(this, getString(R.string.msg_fill_all_requierd_fields), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void getBrandListSuccessfully(List<Brand> brandList) {
        if (brandList != null) {
            mBrandList = brandList;
            mBrandAdapter.setData(brandList);
            binding.spinnerBrand.setAdapter(mBrandAdapter);
            if (mIntentMode.equals(IntentManager.ModeParams.EXTRA_MODE_EDIT)) {
                setSelectedBrand(mExtraProduct.getProductBrandId());
            }
        } else {
            Toast.makeText(AddOrEditProductActivity.this, "Không có dữ liệu nhãn hàng", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void getBrandListDataFail() {
        Toast.makeText(AddOrEditProductActivity.this, "Lỗi hiển thị", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getTypeListSuccessfully(List<ProductGroup> productGroupList) {
        if (productGroupList != null) {
            mTypeList = productGroupList;
            mTypeAdapter.setData(productGroupList);
            binding.spinnerType.setAdapter(mTypeAdapter);
            if (mIntentMode.equals(IntentManager.ModeParams.EXTRA_MODE_EDIT)) {
                setSelectedType(mExtraProduct.getProductGroupId());
            }
        } else {
            Toast.makeText(AddOrEditProductActivity.this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void getTypeListFail() {
        Toast.makeText(AddOrEditProductActivity.this, "Lỗi hiển thị", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void createProductSuccessfully() {
        mLoadingDialog.hide();
        Toast.makeText(this, "Tạo sản phẩm thành công", Toast.LENGTH_SHORT).show();
        setResult(Activity.RESULT_OK, new Intent());
        finish();
    }

    @Override
    public void createProductFail() {
        mLoadingDialog.hide();
        Toast.makeText(this, "Tạo sản phẩm thất bại", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getExtraProductSuccessfully(Product product) {
        this.mExtraProduct = product;
        binding.textProductName.setText(product.getProductName());
        binding.textProductOutPrice.setText(String.valueOf(product.getOutPrice()));
        binding.textProductDescription.setText(product.getDescription());
        binding.textProductQuantity.setText(String.valueOf(product.getActualQuantity()));

        if (product.getStatus() == (byte) 1) {
            binding.buttonInStock.setSelected(true);
            binding.buttonOutStock.setSelected(false);
            binding.buttonInStock.setTextColor(ContextCompat.getColor(this, R.color.primaryColor));
            binding.buttonOutStock.setTextColor(ContextCompat.getColor(this, R.color.text_title));
            binding.buttonInStock.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.radio_button_left_checked));
            binding.buttonOutStock.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.radio_button_right_unchecked));
        } else {
            binding.buttonInStock.setSelected(false);
            binding.buttonOutStock.setSelected(true);
            binding.buttonInStock.setTextColor(ContextCompat.getColor(this, R.color.text_title));
            binding.buttonOutStock.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_dark));
            binding.buttonInStock.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.radio_button_left_unchecked));
            binding.buttonOutStock.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.radio_button_right_checked));
        }

        Float inPrice = product.getInPrice();
        if (inPrice > 0.0f) {
            binding.textProductInPrice.setText(String.valueOf(inPrice));
        }

        Float discount = product.getDiscount();
        if (discount > 0.0f) {
            binding.textProductDiscount.setText(String.valueOf(discount));
        }

        String barcode = product.getProductBarcode();
        if (TextUtils.isValidString(barcode)) {
            binding.textProductBarcode.setText(barcode);
        }

        String note = product.getNote();
        if (TextUtils.isValidString(note)) {
            binding.textProductNote.setText(note);
        }

        new Thread(() -> {
            String imagePaths = product.getAvatarPath();

            runOnUiThread(() -> {
                binding.progressBarImage.setVisibility(View.VISIBLE);
                if (TextUtils.isValidString(imagePaths)) {
                    PicassoHelper.getPicassoInstance(this).load(imagePaths)
                            .into(new Target() {
                                @Override
                                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                    binding.progressBarImage.setVisibility(View.GONE);
                                    binding.imageProduct.setImageBitmap(bitmap);
                                    binding.imageProduct.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                                    binding.progressBarImage.setVisibility(View.GONE);
                                    Toast.makeText(AddOrEditProductActivity.this, "Tải ảnh thất bại", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onPrepareLoad(Drawable placeHolderDrawable) {
                                }
                            });
                }
            });
        }).start();
    }

    @Override
    public void deleteProduct() {
        mConfirmDeletionDialog.hide();
        mLoadingDialog.show();
        mPresenter.handleDeleteProduct(mExtraProduct);
    }

    @Override
    public void getExtraProductFail() {
        Toast.makeText(this, "Có lỗi", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void deleteProductSuccessfully() {
        Toast.makeText(this, "Xóa sản phẩm thành công", Toast.LENGTH_SHORT).show();
        setResult(Activity.RESULT_OK, new Intent());
        finish();
    }

    @Override
    public void deleteProductFail() {
        mLoadingDialog.hide();
        Toast.makeText(this, "Xóa sản phẩm thất bại", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateProductSuccessfully() {
        mLoadingDialog.hide();
        Toast.makeText(this, "Cập nhật sản phẩm thành công", Toast.LENGTH_SHORT).show();
        setResult(Activity.RESULT_OK, new Intent());
        finish();
    }

    @Override
    public void updateProductFail() {
        mLoadingDialog.hide();
        Toast.makeText(this, "Cập nhật sản phẩm thất bại", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void uploadTemporaryImage() {
        binding.progressBarImage.setVisibility(View.VISIBLE);
        mPresenter.handleUploadTemporaryImage(this, mSelectedPictureUri);
    }

    @Override
    public void onUploadTemporaryImageSuccess() {
        binding.progressBarImage.setVisibility(View.GONE);
        handleSelectedImages();
    }

    @Override
    public void onUploadTemporaryImageFail() {
        binding.progressBarImage.setVisibility(View.GONE);
        Toast.makeText(this, "Upload ảnh thất bại", Toast.LENGTH_SHORT).show();
    }

    private void registerOnClickListener() {
        binding.actionBack.setOnClickListener(v -> onBackPressed());
        binding.buttonUploadImages.setOnClickListener(v -> onClickRequestGalleryPermission());
        binding.buttonTakePicture.setOnClickListener(v -> onClickRequestCameraPermission());
    }

    private void handleIntent(Intent intent) {
        mIntentMode = intent.getStringExtra(IntentManager.ModeParams.EXTRA_MODE);
        if (TextUtils.isValidString(mIntentMode)) {
            switch (mIntentMode) {
                case IntentManager.ModeParams.EXTRA_MODE_CREATE:
                    // Gán onClickListener cho buttonFinish
                    binding.buttonFinish.setOnClickListener(v -> createProduct());
                    break;

                case IntentManager.ModeParams.EXTRA_MODE_EDIT:
                    // Đổi text cho header
                    binding.textActionBarHeader.setText("Cập nhật dữ liệu");
                    // Hiển thị nút xóa sản phẩm
                    binding.buttonDelete.setVisibility(View.VISIBLE);
                    // Hiển thị divider giữa các button
                    binding.viewDividerButton.setVisibility(View.VISIBLE);
                    // Vô hiệu cập nhật số lương -> Cập nhật trong chức năng kiểm kho
                    binding.textProductQuantity.setEnabled(false);
                    // Gán onClickListener cho buttonFinish
                    binding.buttonFinish.setOnClickListener(v -> updateProduct());
                    // Gán onClickListener cho buttonDelete
                    binding.buttonDelete.setOnClickListener(v -> {
                        View dialogLayout = getLayoutInflater().inflate(R.layout.dialog_confirm_deletion, null);
                        TextView titleDialog = dialogLayout.findViewById(R.id.title_dialog);
                        TextView textNotification = dialogLayout.findViewById(R.id.text_notification);
                        TextView textWarning = dialogLayout.findViewById(R.id.text_warning);
                        MaterialButton buttonCancel = dialogLayout.findViewById(R.id.button_cancel);
                        MaterialButton buttonApprove = dialogLayout.findViewById(R.id.button_approve);

                        titleDialog.setText(R.string.delete_product_dialog_title);
                        textNotification.setText(getString(R.string.msg_delete_product));
                        textWarning.setText(getString(R.string.msg_delete_product_warning));
                        buttonCancel.setOnClickListener(v1 -> mConfirmDeletionDialog.dismiss());
                        buttonApprove.setOnClickListener(v2 -> deleteProduct());

                        mConfirmDeletionDialog.setCancelable(false);
                        mConfirmDeletionDialog.setContentView(dialogLayout);
                        mConfirmDeletionDialog.show();
                    });
                    // Gọi presenter để lấy dữ liệu từ intent
                    mPresenter.getExtraProduct(intent);
                    break;
            }
        }
    }

    public void onRadioButtonClick(View view) {
        boolean isSelected = view.isSelected();
        Integer currentId = mRadioButton.get(view.getId());
        if (currentId != null) {
            switch (currentId) {
                case RADIO_BUTTON_IN_STOCK:
                    if (!isSelected) {
                        binding.buttonInStock.setSelected(true);
                        binding.buttonOutStock.setSelected(false);
                        binding.buttonInStock.setTextColor(ContextCompat.getColor(this, R.color.primaryColor));
                        binding.buttonOutStock.setTextColor(ContextCompat.getColor(this, R.color.text_title));
                    }
                    break;
                case RADIO_BUTTON_OUT_STOCK:
                    if (!isSelected) {
                        binding.buttonInStock.setSelected(false);
                        binding.buttonOutStock.setSelected(true);
                        binding.buttonInStock.setTextColor(ContextCompat.getColor(this, R.color.text_title));
                        binding.buttonOutStock.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_dark));
                    }
                    break;
            }
        }
    }

    private void registerProductValidation() {
        validateProductName();
        validateOutPrice();
        validateDescription();
    }

    private void onFocusHeader() {
        TextUtils.onFocusHeader(this, binding.textHeaderProductName, binding.textProductName);
        TextUtils.onFocusHeader(this, binding.textHeaderOutPrice, binding.textProductOutPrice);
        TextUtils.onFocusHeader(this, binding.textHeaderInPrice, binding.textProductInPrice);
        TextUtils.onFocusHeader(this, binding.textHeaderDiscount, binding.textProductDiscount);
        TextUtils.onFocusHeader(this, binding.textHeaderBarcode, binding.textProductBarcode);
        TextUtils.onFocusHeader(this, binding.textHeaderDes, binding.textProductDescription);
        TextUtils.onFocusHeader(this, binding.textHeaderNote, binding.textProductNote);
        TextUtils.onFocusHeader(this, binding.textHeaderQuantity, binding.textProductQuantity);
    }

    private void validateDescription() {
        binding.textProductDescription.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                handleValidateDescription();
            }
        });

        binding.textProductDescription.addTextChangedListener(new TextWatcherValidation() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handleValidateDescription();
            }
        });
    }

    private boolean handleValidateDescription() {
        String productDescription = TextUtils.getString(binding.textProductDescription);
        ValidationUtils.ValidationResult validationResult = ValidationUtils.validateDescription(productDescription);
        if (!validationResult.isValid()) {
            binding.textLayoutDescription.setError(validationResult.getMessage());
            return false;
        }
        binding.textLayoutDescription.setError(null);
        return true;
    }

    private void validateOutPrice() {
        binding.textProductOutPrice.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                handleValidateOutPrice();
            }
        });

        binding.textProductOutPrice.addTextChangedListener(new TextWatcherValidation() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handleValidateOutPrice();
            }
        });
    }

    private boolean handleValidateOutPrice() {
        String textProductOutPrice = TextUtils.getString(binding.textProductOutPrice);
        ValidationUtils.ValidationResult validationResult = ValidationUtils.validatePrice(textProductOutPrice);

        if (!validationResult.isValid()) {
            binding.textLayoutOutPrice.setError(validationResult.getMessage());
            return false;
        } else {
            binding.textLayoutOutPrice.setError(null);
            return true;
        }
    }

    private void validateProductName() {
        binding.textProductName.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                handleValidateProductName();
            }
        });

        binding.textProductName.addTextChangedListener(new TextWatcherValidation() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handleValidateProductName();
            }
        });
    }

    private boolean handleValidateProductName() {
        String productName = TextUtils.getString(binding.textProductName);
        ValidationUtils.ValidationResult validationUtils = ValidationUtils.validateName(productName);
        if (!validationUtils.isValid()) {
            binding.textLayoutProductName.setError(validationUtils.getMessage());
            return false;
        } else {
            binding.textLayoutProductName.setError(null);
            return true;
        }
    }

    private void setSelectedBrand(long id) {
        int n = mBrandList.size();
        int fMinus2 = 0;
        int fMinus1 = 1;
        int fCurrent = fMinus2 + fMinus1;

        while (fCurrent < n) {
            fMinus2 = fMinus1;
            fMinus1 = fCurrent;
            fCurrent = fMinus2 + fMinus1;
        }

        int offset = -1;
        while (fCurrent > 1) {
            int i = Math.min(offset + fMinus2, n - 1);
            Brand currentBrand = mBrandList.get(i);

            if (currentBrand.getId() < id) {
                fCurrent = fMinus1;
                fMinus1 = fMinus2;
                fMinus2 = fCurrent - fMinus1;
                offset = i;
            } else if (currentBrand.getId() > id) {
                fCurrent = fMinus2;
                fMinus1 = fMinus1 - fMinus2;
                fMinus2 = fCurrent - fMinus1;
            } else {
                binding.spinnerBrand.setSelection(i);
                return;
            }
        }

        if (fMinus1 == 1 && mBrandList.get(offset + 1).getId() == id) {
            binding.spinnerBrand.setSelection(offset + 1);
        }
    }

    private void setSelectedType(long id) {
        int n = mTypeList.size();
        int fMinus2 = 0;
        int fMinus1 = 1;
        int fCurrent = fMinus2 + fMinus1;

        while (fCurrent < n) {
            fMinus2 = fMinus1;
            fMinus1 = fCurrent;
            fCurrent = fMinus2 + fMinus1;
        }

        int offset = -1;
        while (fCurrent > 1) {
            int i = Math.min(offset + fMinus2, n - 1);
            ProductGroup currentType = mTypeList.get(i);

            if (currentType.getId() < id) {
                fCurrent = fMinus1;
                fMinus1 = fMinus2;
                fMinus2 = fCurrent - fMinus1;
                offset = i;
            } else if (currentType.getId() > id) {
                fCurrent = fMinus2;
                fMinus1 = fMinus1 - fMinus2;
                fMinus2 = fCurrent - fMinus1;
            } else {
                binding.spinnerType.setSelection(i);
                return;
            }
        }

        if (fMinus1 == 1 && mTypeList.get(offset + 1).getId() == id) {
            binding.spinnerType.setSelection(offset + 1);
        }
    }

    private void onClickRequestGalleryPermission() {
        if (checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES)
                == PackageManager.PERMISSION_GRANTED) {
            PermissionManager.openGallery(mOpenGalleryLauncher);
        } else {
            requestPermissions(new String[]{Manifest.permission.READ_MEDIA_IMAGES},
                    PermissionManager.REQUEST_PERMISSION_MEDIA_IMAGE);
        }
    }

    private void onClickRequestCameraPermission() {
        if (checkSelfPermission(Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            mTakePictureLauncher.launch(mSelectedPictureUri);
        } else {
            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    PermissionManager.REQUEST_PERMISSION_CAMERA);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionManager.REQUEST_PERMISSION_MEDIA_IMAGE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                PermissionManager.openGallery(mOpenGalleryLauncher);
            }
        } else if (requestCode == PermissionManager.REQUEST_PERMISSION_CAMERA) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mTakePictureLauncher.launch(mSelectedPictureUri);
            }
        } else {
            Toast.makeText(this, "Không có quyền truy cập, vui lòng kiểm tra lại", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleSelectedImages() {
        // Chạy trên thread khác tránh block thread chính dẫn tới crash ứng dụng.
        new Thread(() -> {
            try {
                // ImageDecode.Source tạo đường dẫn từ uri
                ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), mSelectedPictureUri);
                // Sử dụng ImageDecoder.decodeBitmap thay cho MediaStore.Images.Media.getBitmap
                Bitmap bitmap = ImageDecoder.decodeBitmap(source);
                runOnUiThread(() -> {
                    binding.imageProduct.setImageBitmap(bitmap);
                    binding.imageProduct.setVisibility(View.VISIBLE);
                });
            } catch (IOException e) {
                Log.e(TAG, "handleSelectedImages: " + e.getMessage());
            }
        }).start();
    }

    private void registerOpenGalleryLauncher() {
        mOpenGalleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), o -> {
                    if (o.getResultCode() == Activity.RESULT_OK) {
                        Intent data = o.getData();
                        if (data != null) {
                            mSelectedPictureUri = data.getData();
                            uploadTemporaryImage();
                        }
                    }
                }
        );
    }

    private void registerTakePictureLauncher() {
        mTakePictureLauncher = registerForActivityResult(
                new ActivityResultContracts.TakePicture(), result -> {
                    try {
                        if (result) {
                            handleSelectedImages();
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "registerTakePictureLauncher: " + e.getMessage());
                    }
                }
        );
    }

    @SuppressLint("SetTextI18n")
    private void handleFeatureByRole() {
        if (PrefsUtils.getRoldId(mPrefs) == 2) {
            binding.textActionBarHeader.setText("Chi tiết sản phẩm");

            binding.layoutActionButton.setVisibility(View.GONE);
            binding.layoutPhotoButton.setVisibility(View.GONE);

            TextUtils.disableEditText(binding.textProductName);
            TextUtils.disableEditText(binding.textProductOutPrice);
            TextUtils.disableEditText(binding.textProductInPrice);
            TextUtils.disableEditText(binding.textProductDiscount);
            TextUtils.disableEditText(binding.textProductQuantity);
            TextUtils.disableEditText(binding.textProductBarcode);
            TextUtils.disableEditText(binding.textProductDescription);
            TextUtils.disableEditText(binding.textProductNote);

            binding.spinnerBrand.setEnabled(false);
            binding.spinnerType.setEnabled(false);

            for (int i = 0; i < binding.radioGroupStock.getChildCount(); i++) {
                RadioButton child = (RadioButton) binding.radioGroupStock.getChildAt(i);
                child.setEnabled(false);
            }
        }
    }

}