package com.example.doanmonhoc.utils;

import android.util.Log;

import java.util.Objects;

public final class NumberUtils {
    public static final String TAG = "NumberUtils";

    public static float parseFloatOrDefault(final String text) {
        return parseFloatOrDefault(text, 0.0f);
    }

    public static float parseFloatOrDefault(final String text, final float defaultValue) {
        if (text.isEmpty()) {
            return defaultValue;
        }

        try {
            return Float.parseFloat(text);
        } catch (NumberFormatException e) {
            Log.e(TAG, Objects.requireNonNull(e.getMessage()));
            return defaultValue;
        }
    }

    public static int parseIntegerOrDefault(final String text) {
        return parseIntegerOrDefault(text, 0);
    }

    public static int parseIntegerOrDefault(final String text, final int defaultValue) {
        if (text.isEmpty()) {
            return defaultValue;
        }

        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            Log.e(TAG, Objects.requireNonNull(e.getMessage()));
            return defaultValue;
        }
    }
}
