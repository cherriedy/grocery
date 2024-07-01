package com.example.doanmonhoc.utils;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.activity.result.ActivityResultLauncher;
import androidx.core.content.FileProvider;

import java.io.File;
import java.util.UUID;

public class PermissionManager {
    public static final int NOT_REQUEST_PERMISSION = -1;
    public static final int REQUEST_PERMISSION_MEDIA_IMAGE = 0;
    public static final int REQUEST_PERMISSION_CAMERA = 1;

    public static boolean isRequestCode(int permission) {
        return permission != NOT_REQUEST_PERMISSION;
    }

    public static void openGallery(ActivityResultLauncher<Intent> activityResultLauncher) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activityResultLauncher.launch(intent);
    }

    public static Uri createUriForTakePicture(Context context) {
        String uniqueFileName = UUID.randomUUID().toString();
        File image = new File(context.getFilesDir(), uniqueFileName + "jpeg");
        return FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", image);
    }
}
