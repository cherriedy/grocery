package com.example.doanmonhoc.utils;

import android.util.Log;

public class Utils {
    public static int extraKeyNumber(String latestKey, String prefix) {
        if (latestKey.startsWith(prefix)) {
            String numberPart = latestKey.substring(prefix.length());
            try {
                return Integer.parseInt(numberPart);
            } catch (NumberFormatException e) {
                Log.e("ExtraKeyNumber", e.getMessage());
            }
        }
        return 0;
    }

    public static String formatKey(int numberKey, String prefix) {
        if (TextUtils.isValidString(prefix)) {
            return String.format(prefix + "%03d", numberKey);
        }
        return "";
    }
}
