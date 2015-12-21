package com.ikaowo.join.network;

import com.ikaowo.join.model.request.LoginRequest;
import com.ikaowo.join.model.response.LoginResponse;
import com.ikaowo.join.model.response.UserInfoResponse;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;

/**
 * Created by weibo on 15-12-2.
 */
public interface TestInterface {

  @GET("v1/user")
  Call<UserInfoResponse> getUserInfo();

  @POST("v1/user/login2")
  Call<LoginResponse> login(@Body LoginRequest request);
}
