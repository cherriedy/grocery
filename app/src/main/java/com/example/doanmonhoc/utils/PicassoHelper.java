package com.example.doanmonhoc.utils;

import android.content.Context;
import android.widget.ImageView;

import com.example.doanmonhoc.R;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class PicassoHelper {

    private PicassoHelper() {
    }

    private static OkHttpClient getOkHttpClient() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);

        return new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(loggingInterceptor)
                .build();
    }

    public static Picasso getPicassoInstance(Context context) {
        OkHttpClient okHttpClient = getOkHttpClient();

        return new Picasso.Builder(context)
                .downloader(new OkHttp3Downloader(okHttpClient))
                .build();
    }

    public static void loadImage(Context context, String url, ImageView imageView) {
        getPicassoInstance(context)
                .load(url)
                .placeholder(R.drawable.img_no_image)
                .error(R.drawable.img_no_image)
                .into(imageView);
    }

    public static void loadImageWithOptions(Context context, String url, ImageView imageView,
                                            int placeholderResId, int errorResId) {
        getPicassoInstance(context)
                .load(url)
                .placeholder(placeholderResId)
                .error(errorResId)
                .into(imageView);
    }
}
