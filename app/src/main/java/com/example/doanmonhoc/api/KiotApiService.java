package com.example.doanmonhoc.api;

import android.database.Observable;

import com.example.doanmonhoc.model.Brand;
import com.example.doanmonhoc.model.Product;
import com.example.doanmonhoc.model.ProductGroup;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface KiotApiService {

    HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

    OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .addInterceptor(httpLoggingInterceptor)
            .build();

    // http://cherrapi.onlinewebshop.net/
    final String BASE_URL = "http://cherrapi.onlinewebshop.net";

    // Create Gson instance with GsonBuilder()
    Gson gson = new GsonBuilder()
            .setDateFormat("dd/mm/YYYY HH:mm:ss")   // Set the datetime format
            .create();

    KiotApiService apiService = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
            .create(KiotApiService.class);

    // Product API
    @GET("/product")
    Call<List<Product>> getProductList();

    @GET("/product/{id}")
    Call<Product> getDetailedProduct(@Path("id") int productID);

    @GET("/product/latest")
    Call<Product> getLatestProduct();

    @POST("/product")
    Call<Product> createProduct(@Body Product product);

    @PATCH("/product/{id}")
    Call<Product> updateProduct(@Path("id") int productID, @Body Product newProduct);

    @DELETE("/product/{id}")
    Call<Product> deleteProduct(@Path("id") int productID);

    // BrandOfProduct API
    @GET("/brand")
    Call<List<Brand>> getBrandList();

    // ProductGroup API
    @GET("/type")
    Call<List<ProductGroup>> getProductGroupList();
}
