package com.example.doanmonhoc.activity.ProductManagement;

import android.content.res.Resources;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.doanmonhoc.R;
import com.example.doanmonhoc.databinding.ActivityAddProductBinding;

public class AddProductActivity extends AppCompatActivity {

    ActivityAddProductBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityAddProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        setContentView(R.layout.activity_add_product);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        Resources resources = getResources();

        String[] mockBrandList = resources.getStringArray(R.array.mockBrands);
        ArrayAdapter<String> brandListAdapter = new ArrayAdapter<>(AddProductActivity.this,R.layout.dropdown_item, mockBrandList) ;
        binding.acvBrand.setAdapter(brandListAdapter);

        String[] mockTypeList = resources.getStringArray(R.array.mockTypes);
        ArrayAdapter<String>  typeListAdapter = new ArrayAdapter<>(AddProductActivity.this, R.layout.dropdown_item, mockTypeList);
        binding.acvType.setAdapter(typeListAdapter);
    }
}