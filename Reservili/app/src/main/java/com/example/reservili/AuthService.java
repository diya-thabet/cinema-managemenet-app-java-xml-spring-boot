package com.example.reservili;

import com.example.reservili.ui.login.LoginRequest;
import com.example.reservili.ui.login.LoginResponse;
import com.example.reservili.ui.signup.SignupRequest;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthService {
    @POST("auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);
    @POST("auth/signup")
    Call<Map<String, Integer>> signup(@Body SignupRequest request);
}
