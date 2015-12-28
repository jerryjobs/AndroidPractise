package com.ikaowo.join.network;

import com.ikaowo.join.model.base.BaseResponse;
import com.ikaowo.join.model.response.PromptionListResposne;

import java.util.Map;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.QueryMap;

/**
 * Created by weibo on 15-12-23.
 */
public interface PromptionInterface {


  @POST("social/publish")
  Call<BaseResponse> postPromption(@Body Map request);

  //公司发布的活动列表&首页推荐列表
  @GET("social/info")
  Call<PromptionListResposne> getPromptionList(@QueryMap Map map);

  //公司参加过的活动列表
  @GET("social/Detailinfo/3/{brand_id}/{cp}/{ps}")
  Call<PromptionListResposne> getJoinedPromptionList(@Path("brand_id") int brandId, @Path("cp") int cp, @Path("ps") int ps);

}
