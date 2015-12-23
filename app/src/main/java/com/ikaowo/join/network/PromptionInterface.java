package com.ikaowo.join.network;

import com.ikaowo.join.model.base.BaseResponse;

import java.util.Map;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by weibo on 15-12-23.
 */
public interface PromptionInterface {

  @POST("social/publish")
  Call<BaseResponse> postPromption(@Body Map request);
}
