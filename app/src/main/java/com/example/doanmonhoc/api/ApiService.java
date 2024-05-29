package com.example.doanmonhoc.api;

import com.example.doanmonhoc.Model.Account;
import com.example.doanmonhoc.Model.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("login.php")
    Call<LoginResponse> loginUser(@Body Account account);

}

