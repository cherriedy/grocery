package com.example.doanmonhoc.activity.ProductManagement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.doanmonhoc.R;
import com.example.doanmonhoc.adapter.BrandAutoCompleteAdapter;
import com.example.doanmonhoc.adapter.TypeAutoCompleteAdapter;
import com.example.doanmonhoc.contract.ProductManagement.ProductAddContract;
import com.example.doanmonhoc.databinding.ActivityAddProductBinding;
import com.example.doanmonhoc.model.Brand;
import com.example.doanmonhoc.model.Product;
import com.example.doanmonhoc.model.ProductGroup;
import com.example.doanmonhoc.presenter.ProductManagament.ProductAddPresenter;
import com.example.doanmonhoc.utils.ExtraManager;
import com.example.doanmonhoc.utils.NumberUtils;
import com.example.doanmonhoc.utils.TextUtils;
import com.example.doanmonhoc.utils.validation.TextWatcherValidation;
import com.example.doanmonhoc.utils.validation.ValidationUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddProductActivity extends AppCompatActivity implements ProductAddContract.View {
    public static final int RADIO_BUTTON_IN_STOCK = 0;
    public static final int RADIO_BUTTON_OUT_STOCK = 1;
    private Map<Integer, Integer> radioButtons;

    private int brandClickItemId = -1;
    private int productGroupClickItemId = -1;

    private ActivityAddProductBinding binding;
    private ProductAddPresenter productAddPresenter;

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
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));

        productAddPresenter = new ProductAddPresenter(this);

        radioButtons = new HashMap<>();
        radioButtons.put(R.id.button_inStock, RADIO_BUTTON_IN_STOCK);
        radioButtons.put(R.id.button_outStock, RADIO_BUTTON_OUT_STOCK);

        onFocusHeader();
        callValidateProduct();
        handleIntent(getIntent());
    }

    @Override
    public void createProduct() {
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
        product.setProductKey(productAddPresenter.generateLatestProductKey());
        product.setProductName(nameText);
        product.setOutPrice(outPrice);
        product.setProductNote(noteText);
        product.setDescription(descriptionText);
        product.setProductBrandId(brandClickItemId);
        product.setProductGroupId(productGroupClickItemId);

        Log.i("InStock", String.valueOf(binding.buttonInStock.isSelected()));
        Log.i("OutStock", String.valueOf(binding.buttonOutStock.isSelected()));
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
            productAddPresenter.handleCreateProduct(product);
            setResult(Activity.RESULT_OK, new Intent());
            finish();
        } else {
            Toast.makeText(this, getString(R.string.msg_fill_all_requierd_fields), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Lấy dữ liệu từ database hiển thị lên AutoComplete
        productAddPresenter.getBrandList();
        productAddPresenter.getProductGroupList();
        // Lấy id của item trong AutoComplete theo database
        getBrandItemId();
        getProductGroupItemId();
    }

    @Override
    public void getBrandAutoCompleteDataSuccessfully(List<Brand> brandList) {
        if (brandList != null) {
            BrandAutoCompleteAdapter adapter = new BrandAutoCompleteAdapter(AddProductActivity.this, R.layout.dropdown_item, brandList);
            binding.autoCompleteBrand.setAdapter(adapter);
        } else {
            Toast.makeText(AddProductActivity.this, "Không có dữ liệu nhãn hàng", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void getBrandAutoCompleteDataFail() {
        Toast.makeText(AddProductActivity.this, "Lỗi hiển thị", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getProductGroupAutoCompleteDataSuccessfully(List<ProductGroup> productGroupList) {
        if (productGroupList != null) {
            TypeAutoCompleteAdapter adapter = new TypeAutoCompleteAdapter(AddProductActivity.this, R.layout.dropdown_item, productGroupList);
            binding.autoCompleteType.setAdapter(adapter);
        } else {
            Toast.makeText(AddProductActivity.this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void getBrandItemId() {
        binding.autoCompleteBrand.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, android.view.View view, int position, long id) {
                Brand selectedBrand = (Brand) parent.getItemAtPosition(position);
                brandClickItemId = selectedBrand.getId();
            }
        });
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
        Toast.makeText(AddProductActivity.this, "Lỗi hiển thị", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void notifyCreateProductSuccessfully() {
        Toast.makeText(this, "Tạo sản phẩm thành công", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void notifyCreateProductFail() {
        Toast.makeText(this, "Tạo sản phẩm thất bại", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getExtraProductSuccessfully(Product receivedExtraProduct) {
        binding.textProductName.setText(receivedExtraProduct.getProductName());
        binding.textProductOutPrice.setText(String.valueOf(receivedExtraProduct.getOutPrice()));
        binding.textProductDescription.setText(receivedExtraProduct.getDescription());

        Float inPrice = receivedExtraProduct.getInPrice();
        if (inPrice > 0.0f) {
            binding.textProductInPrice.setText(String.valueOf(inPrice));
        }

        Float discount = receivedExtraProduct.getDiscount();
        if (discount > 0.0f) {
            binding.textProductDiscount.setText(String.valueOf(discount));
        }

        String barcode = receivedExtraProduct.getProductBarcode();
        if (barcode != null && !barcode.isEmpty()) {
            binding.textProductBarcode.setText(barcode);
        }

        String note = receivedExtraProduct.getNote();
        if (note != null && !note.isEmpty()) {
            binding.textProductNote.setText(note);
        }

    }

    @Override
    public void getExtraProductFail() {
        Toast.makeText(this, "Có lỗi", Toast.LENGTH_SHORT).show();
    }

    private void callValidateProduct() {
        validateProductName();
        validateOutPrice();
        validateProductBrand();
        validateProductType();
        validateDescription();
    }

    private void handleIntent(Intent intent) {
        String intentMode = intent.getStringExtra(ExtraManager.ModeParams.EXTRA_MODE);
        if (intentMode != null && !intentMode.isEmpty()) {
            switch (intentMode) {
                case ExtraManager.ModeParams.EXTRA_MODE_CREATE:
                    productAddPresenter.getCurrentLatestProductKey();
                    binding.buttonCreate.setOnClickListener(v -> createProduct());
                    break;

                case ExtraManager.ModeParams.EXTRA_MODE_EDIT:
                    // Đổi text cho header
                    binding.textActionBarHeader.setText("Cập nhật dữ liệu");
                    // Hiển thị nút xóa sản phẩm
                    binding.buttonDelete.setVisibility(View.VISIBLE);
                    binding.viewDividerButton.setVisibility(View.VISIBLE);
                    // Gọi presenter để lấy dữ liệu từ intent
                    productAddPresenter.getExtraProduct(intent);
                    break;
            }
        }
    }

    private void onFocusHeader() {
        TextUtils.onFocusHeader(this, binding.textHeaderProductName, binding.textProductName);
        TextUtils.onFocusHeader(this, binding.textHeaderOutPrice, binding.textProductOutPrice);
        TextUtils.onFocusHeader(this, binding.textHeaderInPrice, binding.textProductInPrice);
        TextUtils.onFocusHeader(this, binding.textHeaderDiscount, binding.textProductDiscount);
        TextUtils.onFocusHeader(this, binding.textHeaderBarcode, binding.textProductBarcode);
        TextUtils.onFocusHeader(this, binding.textHeaderDes, binding.textProductDescription);
        TextUtils.onFocusHeader(this, binding.textHeaderNote, binding.textProductNote);
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
        // Kiểm tra dữ liệu được nhập vào khi không còn foucs
        binding.textProductName.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                handleValidateProductName();
            }
        });

        // Kiểm tra dữ liệu sau khi kết thúc nhập
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
        String brandText = binding.autoCompleteBrand.getText().toString().trim();
        if (!brandText.isEmpty()) {
            binding.textLayoutAutoCompleteBrand.setError(null);
            return true;
        }
        binding.textLayoutAutoCompleteBrand.setError(getString(R.string.msg_product_brand_requirement));
        return false;
    }

    private void validateProductBrand() {
        binding.autoCompleteBrand.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                handleValidateProductBrand();
            }
        });

        binding.autoCompleteBrand.addTextChangedListener(new TextWatcherValidation() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handleValidateProductBrand();
            }
        });
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

    public void onRadioButtonClick(View view) {
        boolean isSelected = view.isSelected();
        Integer currentId = radioButtons.get(view.getId());
        if (currentId != null) {
            switch (currentId) {
                case RADIO_BUTTON_IN_STOCK:
                    if (!isSelected) {
                        binding.buttonInStock.setSelected(true);
                        binding.buttonOutStock.setSelected(false);
                        binding.buttonInStock.setTextColor(ContextCompat.getColor(this, R.color.primaryColor));
                        binding.buttonOutStock.setTextColor(ContextCompat.getColor(this, R.color.textHeading));
                    }
                    break;
                case RADIO_BUTTON_OUT_STOCK:
                    if (!isSelected) {
                        binding.buttonInStock.setSelected(false);
                        binding.buttonOutStock.setSelected(true);
                        binding.buttonInStock.setTextColor(ContextCompat.getColor(this, R.color.textHeading));
                        binding.buttonOutStock.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_dark));
                    }
                    break;
            }
        }
    }
}