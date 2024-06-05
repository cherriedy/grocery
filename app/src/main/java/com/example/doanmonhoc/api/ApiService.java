package com.example.doanmonhoc.api;

import com.example.doanmonhoc.model.Account;
import com.example.doanmonhoc.model.LoginResponse;
import com.example.doanmonhoc.model.Staff;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {
    @POST("login")
    Call<LoginResponse> loginUser(@Body Account account);

    @GET("staff/{id}")
    Call<Staff> getStaffById(@Path("id") long id);

    @PUT("staff/{id}")
    Call<Staff> updateStaff(@Path("id") long id, @Body Staff staff);


}

