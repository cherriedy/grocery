package com.example.doanmonhoc.utils;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;

public class PrefsUtils {
    public static final String PREFS = "myPrefs";
    public static final String PREFS_ID = "id";
    public static final String PREFS_IMAGE = "staffImage";
    public static final String PREFS_NAME = "staffName";
    public static final String PREFS_ROLE = "Roleid";

    public static long getId(@NonNull SharedPreferences prefs) {
        return prefs.getLong(PREFS_ID, -1);
    }

    public static long getRoleId(@NonNull SharedPreferences prefs) {
        return prefs.getLong(PREFS_ROLE, -1);
    }

    public static String getName(@NonNull SharedPreferences prefs) {
        return prefs.getString(PREFS_NAME, "");
    }
}
