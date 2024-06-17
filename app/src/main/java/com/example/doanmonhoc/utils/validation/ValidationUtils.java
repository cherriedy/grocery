package com.example.doanmonhoc.utils.validation;

import android.util.Log;

import com.example.doanmonhoc.utils.NumberUtils;

public class ValidationUtils {
    public static final int MAX_NAME_LENGTH = 64;
    public static final int MAX_DESCRIPTION_LENGTH = 128;

    public static class ValidationResult {
        private final boolean isValid;
        private final String message;

        public ValidationResult(boolean isValid, String message) {
            this.isValid = isValid;
            this.message = message;
        }

        public boolean isValid() {
            return isValid;
        }

        public String getMessage() {
            return message;
        }
    }

    public static ValidationResult validateName(String name) {
        if (!isValidString(name)) {
            return new ValidationResult(false, "Không để trống tên");
        } else if (name.length() > MAX_NAME_LENGTH) {
            return new ValidationResult(false, "Độ dài không quá " + MAX_NAME_LENGTH + " kí tự");
        }
        return new ValidationResult(true, null);
    }

    public static ValidationResult validateDescription(String description) {
        if (!isValidString(description)) {
            return new ValidationResult(false, "Không để trống mô tả");
        } else if (description.length() > MAX_DESCRIPTION_LENGTH) {
            return new ValidationResult(false, "Độ dài không quá " + MAX_DESCRIPTION_LENGTH + " kí tự");
        }
        return new ValidationResult(true, null);
    }

    public static ValidationResult validatePrice(String priceText) {
        Log.i("Price", String.valueOf(priceText));
        if (!isValidString(priceText)) {
            return new ValidationResult(false, "Không để trống giá");
        } else {
            float price = NumberUtils.parseFloatOrDefault(priceText);
            Log.i("PriceF", String.valueOf(price));
            if (price <= 0.0f) {
                return new ValidationResult(false, "Giá không hợp lệ");
            }
        }
        return new ValidationResult(true, null);
    }

    private static boolean isValidString(String text) {
        return text != null && !text.isEmpty();
    }
}
