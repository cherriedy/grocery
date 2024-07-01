package com.example.doanmonhoc.api;


import static com.example.doanmonhoc.activity.Auth.ForgotPasswordActivity.API_KEY;
import static com.example.doanmonhoc.activity.Auth.ForgotPasswordActivity.API_SECRET;

import android.util.Base64;

import com.example.doanmonhoc.model.OTPRequest;
import com.example.doanmonhoc.model.OTPResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface MailjetApiService {
    String BASE_URL = "https://api.mailjet.com/v3.1/";

    HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd")
            .create();

    OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(chain -> {
                Request original = chain.request();
                Request.Builder builder = original.newBuilder()
                        .header("Authorization", "Basic " + Base64.encodeToString((API_KEY + ":" + API_SECRET).getBytes(), Base64.NO_WRAP));
                Request request = builder.build();
                return chain.proceed(request);
            })
            .build();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build();

    MailjetApiService api = retrofit.create(MailjetApiService.class);

    @Headers("Content-Type: application/json")
    @POST("send")
    Call<OTPResponse> sendEmail(@Body OTPRequest request);
}
