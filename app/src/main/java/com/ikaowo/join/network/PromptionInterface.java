package com.ikaowo.join.network;

import com.ikaowo.join.model.base.BaseResponse;
import com.ikaowo.join.model.response.PromptionListResposne;

import java.util.Map;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.QueryMap;

/**
 * Created by weibo on 15-12-23.
 */
public interface PromptionInterface {

  @POST("social/publish")
  Call<BaseResponse> postPromption(@Body Map request);

  @GET("social/info")
  Call<PromptionListResposne> getPromptionList(@QueryMap Map map);
}
