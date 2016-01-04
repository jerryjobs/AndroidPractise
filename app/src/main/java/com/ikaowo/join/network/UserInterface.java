package com.ikaowo.join.network;

import com.ikaowo.join.model.base.BaseResponse;
import com.ikaowo.join.model.request.CheckStateRequest;
import com.ikaowo.join.model.request.LoginRequest;
import com.ikaowo.join.model.request.ResetPasswdRequest;
import com.ikaowo.join.model.request.SignupRequest;
import com.ikaowo.join.model.request.UpdateAvatarRequest;
import com.ikaowo.join.model.request.UpdatePasswordRequest;
import com.ikaowo.join.model.response.CheckStateResponse;
import com.ikaowo.join.model.response.SignupResponse;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.POST;
import retrofit.http.PUT;

/**
 * Created by weibo on 15-12-21.
 */
public interface UserInterface {
  @POST("user/login")
  Call<SignupResponse> signin(@Body LoginRequest request);

  @POST("user/signup")
  Call<SignupResponse> signup(@Body SignupRequest request);

  //忘记密码重置
  @PUT("user/fgpw")
  Call<BaseResponse> resetPasswd(@Body ResetPasswdRequest request);

  //修改
  @PUT("user/password")
  Call<BaseResponse> updatePasswd(@Body UpdatePasswordRequest request);

  @PUT("user/user_info")
  Call<BaseResponse> updateAvatar(@Body UpdateAvatarRequest request);

  @PUT("user/session")
  Call<CheckStateResponse> checkLatestState(@Body CheckStateRequest request);
}
