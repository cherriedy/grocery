package com.example.doanmonhoc.utils;

public class NumberUtils {
    public static float parseFloatOrDefault(final String text) {
        return parseFloatOrDefault(text, 0.0f);
    }

    public static float parseFloatOrDefault(final String text, final float defaultValue) {
        if (text.isEmpty()) { return defaultValue; }

        try {
            return Float.parseFloat(text);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
