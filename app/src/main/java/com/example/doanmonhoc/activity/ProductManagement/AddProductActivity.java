package com.example.doanmonhoc.activity.ProductManagement;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.doanmonhoc.R;
import com.example.doanmonhoc.adapter.BrandAutoCompleteAdapter;
import com.example.doanmonhoc.adapter.TypeAutoCompleteAdapter;
import com.example.doanmonhoc.contract.ProductAddContract;
import com.example.doanmonhoc.databinding.ActivityAddProductBinding;
import com.example.doanmonhoc.model.Brand;
import com.example.doanmonhoc.model.Product;
import com.example.doanmonhoc.model.ProductGroup;
import com.example.doanmonhoc.presenter.ProductAddPresenter;

import java.util.List;

public class AddProductActivity extends AppCompatActivity implements ProductAddContract.View {

    private int brandClickItemId = -1;
    private int productGroupClickItemId = -1;
    private ActivityAddProductBinding b;
    private List<Brand> brandList;
    private List<ProductGroup> productGroupList;
    ProductAddPresenter productAddPresenter = new ProductAddPresenter(AddProductActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        b = ActivityAddProductBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        onFocusProductName();
        onTextChangeProductName();

        onFocusOutPrice();
        onTextChangeOutPrice();

        Intent intent = getIntent();
        String intentMode = intent.getStringExtra("MODE");
        if (intentMode.equals(ProductManagementActivity.EXTRA_MODE_CREATE)) {
            productAddPresenter.getCurrentLatestProductKey();
            b.buttonCreate.setOnClickListener(v -> getNewProductInformation());
        } else if (intentMode.equals(ProductManagementActivity.EXTRA_MODE_UPDATE)) {
            b.textActionBarHeader.setText("Cập nhật dữ liệu");
            productAddPresenter.getExtraProduct(intent);
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
            b.autoCompleteBrand.setAdapter(adapter);
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
            b.autoCompleteType.setAdapter(adapter);
        } else {
            Toast.makeText(AddProductActivity.this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void getBrandItemId() {
        b.autoCompleteBrand.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, android.view.View view, int position, long id) {
                Brand selectedBrand = (Brand) parent.getItemAtPosition(position);
                brandClickItemId = selectedBrand.getId();
            }
        });
    }

    @Override
    public void getProductGroupItemId() {
        b.autoCompleteType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
    public void getNewProductInformation() {
        String nameText = b.textProductName.getText().toString().trim();
        String barcodeText = b.textProductBarcode.getText().toString().trim();
        String descriptionText = b.textProductDescription.getText().toString().trim();
        String noteText = b.textProductNote.getText().toString().trim();
        String outPriceText = b.textProductOutPrice.getText().toString().trim();
        String inPriceText = b.textProductInPrice.getText().toString().trim();
        String discountText = b.textProductDiscount.getText().toString().trim();

        float outPrice = parseFloatOrDefault(outPriceText);
        float inPrice = parseFloatOrDefault(inPriceText);
        float discount = parseFloatOrDefault(discountText);

        Product product = new Product();
        product.setProductKey(productAddPresenter.generateLatestProductKey());
//        Log.i("PRODUCT_KEY", product.getProductKey());
        product.setProductName(nameText);
        product.setOutPrice(outPrice);
        product.setProductNote(noteText);
        product.setDescription(descriptionText);
        product.setProductBrandId(brandClickItemId);
        product.setProductGroupId(productGroupClickItemId);

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

        if (validateProductName() && validateOutPrice()) {
            productAddPresenter.createProduct(product);
            setResult(RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, getString(R.string.msg_fill_all_requierd_fields), Toast.LENGTH_SHORT).show();
        }
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
        b.textProductName.setText(receivedExtraProduct.getProductName());
        b.textProductOutPrice.setText(String.valueOf(receivedExtraProduct.getOutPrice()));
        b.textProductDescription.setText(receivedExtraProduct.getDescription());

        Float inPrice = receivedExtraProduct.getInPrice();
        if (inPrice > 0.0f) {
            b.textProductInPrice.setText(String.valueOf(inPrice));
        }

        Float discount = receivedExtraProduct.getDiscount();
        if (discount > 0.0f) {
            b.textProductDiscount.setText(String.valueOf(discount));
        }

        String barcode = receivedExtraProduct.getProductBarcode();
        if (!barcode.isEmpty()) {
            b.textProductBarcode.setText(barcode);
        }

        String note = receivedExtraProduct.getNote();
        if (!note.isEmpty()) {
            b.textProductNote.setText(note);
        }

    }

    @Override
    public void getExtraProductFail() {

    }

    private float parseFloatOrDefault(String text) {
        return parseFloatOrDefault(text, 0.0f);
    }

    private float parseFloatOrDefault(String text, Float defaultValue) {
        try {
            return Float.parseFloat(text);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private void onFocusDescription() {
        b.textProductDescription.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                b.textLayoutDescription.setHelperText(validateDescription());
            }
        });
    }

    private String validateDescription() {
        if (b.textProductDescription.getText().toString().trim().isEmpty()) {
            return getString(R.string.msg_description_requirement);
        } else {
            return null;
        }
    }

    private void onFocusOutPrice() {
        b.textProductOutPrice.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                validateOutPrice();
            }
        });
    }

    private void onTextChangeOutPrice() {
        b.textProductOutPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateOutPrice();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    // Kiểm tra outPrice
    private boolean validateOutPrice() {
        String textProductOutPrice = b.textProductOutPrice.getText().toString().trim();

        if (textProductOutPrice.isEmpty()) {
            b.textLayoutOutPrice.setError(getString(R.string.msg_product_out_price_requirement));
            return false;
        } else {
            b.textLayoutOutPrice.setError(null);
        }

        try {
            float productOutPrice = Float.parseFloat(textProductOutPrice);

            if (productOutPrice <= 0.0f) {
                b.textLayoutOutPrice.setError(getString(R.string.msg_invalid_price));
                return false;
            } else {
                b.textLayoutOutPrice.setError(null);
            }
        } catch (NumberFormatException e) {
            b.textLayoutOutPrice.setError(getString(R.string.msg_invalid_price));
            return false;
        }

        b.textLayoutOutPrice.setError(null);
        return true;
    }

    // Kiểm tra dữ liệu được nhập vào khi không còn foucs
    private void onFocusProductName() {
        b.textProductName.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                validateProductName();
            }
        });
    }

    // Kiểm tra dữ liệu sau khi kết thúc nhập
    private void onTextChangeProductName() {
        b.textProductName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateProductName();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private boolean validateProductName() {
        String productName = b.textProductName.getText().toString().trim();
        if (productName.isEmpty()) {
            b.textLayoutProductName.setError(getString(R.string.msg_product_name_requirement));
            return false;
        } else if (productName.length() > 64) {
            b.textLayoutProductName.setError(getString(R.string.msg_product_name_limitation));
            return false;
        } else {
            b.textLayoutProductName.setError(null);
            return true;
        }
    }
}