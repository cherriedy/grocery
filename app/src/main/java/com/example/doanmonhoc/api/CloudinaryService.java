package com.example.doanmonhoc.api;

import com.example.doanmonhoc.model.CloudinaryUploadResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface CloudinaryService {
    final String BASE_URL = "https://api.cloudinary.com/";

    HttpLoggingInterceptor httpLoggingInterceptor =
            new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd")
            .create();

    OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .addInterceptor(httpLoggingInterceptor)
            .build();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build();

    CloudinaryService apiService = retrofit.create(CloudinaryService.class);

    @Multipart
    @POST("v1_1/dpuldllty/image/upload")
    Call<CloudinaryUploadResponse> uploadImage(@Part MultipartBody.Part file, @Part("upload_preset") RequestBody uploadPreset);

}
