package com.example.doanmonhoc.activity.ProductManagement;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.doanmonhoc.R;
import com.example.doanmonhoc.adapter.BrandAutoCompleteAdapter;
import com.example.doanmonhoc.adapter.BrandSpinnerAdapter;
import com.example.doanmonhoc.adapter.TypeAutoCompleteAdapter;
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
import com.example.doanmonhoc.utils.TextUtils;
import com.example.doanmonhoc.utils.validation.TextWatcherValidation;
import com.example.doanmonhoc.utils.validation.ValidationUtils;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddOrEditProductActivity extends AppCompatActivity implements AddOrEditProductContract.View {
    public static final int RADIO_BUTTON_IN_STOCK = 0;
    public static final int RADIO_BUTTON_OUT_STOCK = 1;
    private static final String TAG = "AddProductActivity";

    private ActivityAddProductBinding binding;
    private String intentMode;
    private int brandClickItemId = 1;
    private int productGroupClickItemId = 1;
    private AddOrEditProductPresenter mPresenter;
    private LoadingDialog mLoadingDialog;
    private Map<Integer, Integer> mRadioButton;
    private Product mExtraProduct;
    private BrandSpinnerAdapter mBrandAdapter;

    private BottomSheetDialog mConfirmDeletionDialog;

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

        onFocusHeader();
        callValidateProduct();
        binding.actionBack.setOnClickListener(v -> onBackPressed());

        mRadioButton = new HashMap<>();
        mLoadingDialog = new LoadingDialog(this);
        mBrandAdapter = new BrandSpinnerAdapter(this);
        mPresenter = new AddOrEditProductPresenter(this);
        mConfirmDeletionDialog = new BottomSheetDialog(this);

        mRadioButton.put(R.id.button_inStock, RADIO_BUTTON_IN_STOCK);
        mRadioButton.put(R.id.button_outStock, RADIO_BUTTON_OUT_STOCK);

        handleIntent(getIntent());
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Lấy dữ liệu từ database hiển thị lên AutoComplete
        mPresenter.getBrandList();
        mPresenter.getProductGroupList();
        // Lấy id của item trong AutoComplete theo database
        getBrandItemId();
        getProductGroupItemId();
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

        Product product = new Product();
        product.setProductName(nameText);
        product.setOutPrice(outPrice);
        product.setProductNote(noteText);
        product.setDescription(descriptionText);
        product.setProductBrandId(brandClickItemId);
        product.setProductGroupId(productGroupClickItemId);

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

        Product product = new Product();
        product.setProductKey(mPresenter.generateLatestProductKey());
        product.setProductName(nameText);
        product.setOutPrice(outPrice);
        product.setProductNote(noteText);
        product.setDescription(descriptionText);
        product.setProductBrandId(brandClickItemId);
        product.setProductGroupId(productGroupClickItemId);
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

        if (handleValidateProductName() && handleValidateOutPrice() && handleValidateDescription()) {
            mLoadingDialog.show();
            mPresenter.handleCreateProduct(product, goodsReceivedNote, detailedGoodsReceivedNote);
        } else {
            Toast.makeText(this, getString(R.string.msg_fill_all_requierd_fields), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void getBrandAutoCompleteDataSuccessfully(List<Brand> brandList) {
        if (brandList != null) {
            BrandAutoCompleteAdapter adapter = new BrandAutoCompleteAdapter(AddOrEditProductActivity.this, R.layout.dropdown_item, brandList);
//            binding.autoCompleteBrand.setAdapter(adapter);
            // TEST SPINNER
            Log.d(TAG, "getBrandAutoCompleteDataSuccessfully: " + brandList.isEmpty());
            mBrandAdapter.setData(brandList);
            Log.d(TAG, "getBrandAutoCompleteDataSuccessfully: " + mBrandAdapter.getData());
            binding.spinnerBrand.setAdapter(mBrandAdapter);
        } else {
            Toast.makeText(AddOrEditProductActivity.this, "Không có dữ liệu nhãn hàng", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void getBrandAutoCompleteDataFail() {
        Toast.makeText(AddOrEditProductActivity.this, "Lỗi hiển thị", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getProductGroupAutoCompleteDataSuccessfully(List<ProductGroup> productGroupList) {
        if (productGroupList != null) {
            TypeAutoCompleteAdapter adapter = new TypeAutoCompleteAdapter(AddOrEditProductActivity.this, R.layout.dropdown_item, productGroupList);
            binding.autoCompleteType.setAdapter(adapter);
        } else {
            Toast.makeText(AddOrEditProductActivity.this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void getBrandItemId() {
//        binding.autoCompleteBrand.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, android.view.View view, int position, long id) {
//                Brand selectedBrand = (Brand) parent.getItemAtPosition(position);
//                brandClickItemId = selectedBrand.getId();
//            }
//        });
    }

    @Override
    public void getProductGroupItemId() {
        binding.autoCompleteType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, android.view.View view, int position, long id) {
                ProductGroup selectedProductGroup = (ProductGroup) parent.getItemAtPosition(position);
                productGroupClickItemId = selectedProductGroup.getId();
            }
        });
    }

    @Override
    public void getProductGroupAutoCompleteDataFail() {
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

        setSelectionBrand(product.getProductBrandId());

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
    }

    @Override
    public void deleteProduct() {
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

    private void handleIntent(Intent intent) {
        intentMode = intent.getStringExtra(IntentManager.ModeParams.EXTRA_MODE);
        if (TextUtils.isValidString(intentMode)) {
            switch (intentMode) {
                case IntentManager.ModeParams.EXTRA_MODE_CREATE:
                    mPresenter.getCurrentLatestProductKey();
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
                        ((TextView) dialogLayout.findViewById(R.id.title_dialog)).setText(getString(R.string.delete_product_dialog_title));
                        ((TextView) dialogLayout.findViewById(R.id.text_notification)).setText(getString(R.string.msg_delete_product));
                        dialogLayout.findViewById(R.id.button_cancel).setOnClickListener(
                                v1 -> mConfirmDeletionDialog.dismiss()
                        );
                        dialogLayout.findViewById(R.id.button_approve).setOnClickListener(
                                v2 -> deleteProduct()
                        );
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

    private void callValidateProduct() {
        validateProductName();
        validateOutPrice();
        validateProductBrand();
        validateProductType();
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

    private boolean handleValidateProductBrand() {
//        String brandText = binding.autoCompleteBrand.getText().toString().trim();
//        if (!brandText.isEmpty()) {
//            binding.textLayoutAutoCompleteBrand.setError(null);
//            return true;
//        }
//        binding.textLayoutAutoCompleteBrand.setError(getString(R.string.msg_product_brand_requirement));
//        return false;
        return true;
    }

    private void validateProductBrand() {
//        binding.autoCompleteBrand.setOnFocusChangeListener((v, hasFocus) -> {
//            if (!hasFocus) {
//                handleValidateProductBrand();
//            }
//        });
//
//        binding.autoCompleteBrand.addTextChangedListener(new TextWatcherValidation() {
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                handleValidateProductBrand();
//            }
//        });
    }

    private boolean handleValidateProductType() {
        String typeText = binding.autoCompleteType.getText().toString().trim();
        if (!typeText.isEmpty()) {
            binding.textLayoutAutoCompleteType.setError(null);
            return true;
        }
        binding.textLayoutAutoCompleteType.setError(getString(R.string.msg_product_type_requirement));
        return false;
    }

    private void validateProductType() {
        binding.autoCompleteType.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                handleValidateProductType();
            }
        });

        binding.autoCompleteType.addTextChangedListener(new TextWatcherValidation() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handleValidateProductType();
            }
        });
    }

    private void setSelectionBrand(long id) {
//        List<Brand> brandList = brandSpinnerAdapter.getData();
//        BrandSpinnerAdapter adpater = (BrandSpinnerAdapter) binding.spinnerBrand.getAdapter();
//        List<Brand> brandList = adpater.getData();
//        Log.d(TAG, "setSelectionBrand: " + brandList);
//        try {
//            if (!brandList.isEmpty()) {
//                for (int i = 0; i < brandList.size(); i++) {
//                    Brand currentBrand = brandList.get(i);
//                    if (currentBrand.getId() == id) {
//                        binding.spinnerBrand.setSelection(i);
//                        return;
//                    }
//                }
//            }
//        } catch (NullPointerException e) {
//            Log.e(TAG, "setSelectionBrand: brandList bị null");
//        }
    }
}