package com.example.doanmonhoc.utils;

import android.content.SharedPreferences;

public class PrefsUtils {
    public static final String PREFS_IMAGE = "staffImage";
    public static final String PREFS_NAME = "staffName";
    public static final String PREFS_ROLE = "Roleid";

    public static long getRoldId(SharedPreferences prefs) {
        return prefs.getLong(PREFS_ROLE, -1);
    }

    public static String getName(SharedPreferences prefs) {
        return prefs.getString(PREFS_NAME, "");
    }
}
