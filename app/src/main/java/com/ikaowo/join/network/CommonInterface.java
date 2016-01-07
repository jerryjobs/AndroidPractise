package com.ikaowo.join.network;

import com.ikaowo.join.model.response.EnumDataResponse;

import retrofit.Call;
import retrofit.http.GET;

/**
 * Created by weibo on 16-1-7.
 */
public interface CommonInterface {
  @GET("system/state")
  Call<EnumDataResponse> getSystemStateEnum();
}
