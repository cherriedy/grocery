package com.example.doanmonhoc.utils;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.doanmonhoc.R;
import com.google.android.material.textfield.TextInputEditText;

public final class TextUtils {
    public static final String TAG = "TextUtils";

    public static boolean isValidString(String text) {
        return text != null && !text.trim().isEmpty();
    }

    public static String getString(TextView tv) {
        String text = tv.getText().toString().trim();
        if (tv == null || !isValidString(text)) {
            Log.e(TAG, "TextView hoặc nội dung là null");
            return "";
        }
        return text;
    }

    public static void onFocusHeader(Context context, TextView header, TextInputEditText textField) {
        textField.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                header.setTextColor(ContextCompat.getColor(context, R.color.primaryColor));
            } else {
                header.setTextColor(ContextCompat.getColor(context, R.color.text_title));
            }
        });
    }
}
