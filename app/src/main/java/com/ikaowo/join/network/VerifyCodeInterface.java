package com.ikaowo.join.network;

import com.ikaowo.join.model.base.BaseResponse;
import com.ikaowo.join.model.request.VerifyCodeRequest;
import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by weibo on 15-12-21.
 */
public interface VerifyCodeInterface {
  @POST("sms/smsapi") Call<BaseResponse> sendVerifyCodeRequest(
      @Body VerifyCodeRequest verifyCodeRequest);
}
