package com.example.doanmonhoc.api;

import com.example.doanmonhoc.model.Account;
import com.example.doanmonhoc.model.Brand;
import com.example.doanmonhoc.model.DetailedGoodsReceivedNote;
import com.example.doanmonhoc.model.GoodsReceivedNote;
import com.example.doanmonhoc.model.LoginResponse;
import com.example.doanmonhoc.model.Product;
import com.example.doanmonhoc.model.ProductGroup;
import com.example.doanmonhoc.model.Staff;
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
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface KiotApiService {
    final String BASE_URL = "http://cherrapi.onlinewebshop.net";

    HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

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

    KiotApiService apiService = retrofit.create(KiotApiService.class);

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
    Call<Product> updateProduct(@Path("id") long productID, @Body Product newProduct);

    @DELETE("/product/{id}")
    Call<Product> deleteProduct(@Path("id") long productID);

    // BrandOfProduct API
    @GET("/brand")
    Call<List<Brand>> getBrandList();

    @POST("/brand")
    Call<Brand> createBrand(@Body Brand brand);

    @GET("/brand/latest")
    Call<Brand> getLatestProductBrand();

    @DELETE("/brand/{id}")
    Call<Brand> deleteBrand(@Path("id") int id);

    // ProductGroup API
    @GET("/type")
    Call<List<ProductGroup>> getProductGroupList();

    @GET("/type/{id}")
    Call<ProductGroup> getProductGroupByID(@Path("id") long id);

    @GET("/type/latest")
    Call<ProductGroup> getLatestProductGroup();

    @POST("/type")
    Call<ProductGroup> createProductGroup(@Body ProductGroup productGroup);

    @PUT("/type/{id}")
    Call<ProductGroup> updateProductGroup(@Path("id") long id, @Body ProductGroup productGroup);

    @DELETE("/type/{id}")
    Call<ProductGroup> deleteProductGroup(@Path("id") long id);

    // Login
    @POST("login")
    Call<LoginResponse> loginUser(@Body Account account);

    @GET("staff/{id}")
    Call<Staff> getStaffById(@Path("id") long id);

    @PUT("staff/{id}")
    Call<Staff> updateStaff(@Path("id") long id, @Body Staff staff);

    @GET("/detailedGoodsReceivedNote")
    Call<List<DetailedGoodsReceivedNote>> getDetailedGoodsReceivedNoteList();


    @GET("/goodsReceivedNote")
    Call<List<GoodsReceivedNote>> getGoodsReceivedNoteList();
}
