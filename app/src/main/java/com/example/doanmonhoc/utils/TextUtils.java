package com.example.doanmonhoc.utils;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.doanmonhoc.R;
import com.google.android.material.textfield.TextInputEditText;

public class TextUtils {
    public static String getString(TextView tv) {
        if (tv == null || tv.getText() == null) {
            Log.e("GetString", "TextView hoặc nội dung là null");
            return "";
        }
        return tv.getText().toString().trim();
    }

    public static void onFocusTextHeading(Context context, TextView tv, TextInputEditText et) {
        et.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                tv.setTextColor(ContextCompat.getColor(context, R.color.primaryColor));
            } else {
                tv.setTextColor(ContextCompat.getColor(context, R.color.textHeading));
            }
        });
    }
}
