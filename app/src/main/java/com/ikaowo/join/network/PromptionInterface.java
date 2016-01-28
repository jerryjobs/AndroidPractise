package com.ikaowo.join.network;

import com.ikaowo.join.model.JoinedUser;
import com.ikaowo.join.model.Promption;
import com.ikaowo.join.model.base.BaseListResponse;
import com.ikaowo.join.model.base.BaseResponse;
import com.ikaowo.join.model.request.CompletePromptionRequest;
import com.ikaowo.join.model.request.JoinRequest;
import com.ikaowo.join.model.request.UpdateJoinStateRequest;
import com.ikaowo.join.model.response.JoinInfoResponse;
import com.ikaowo.join.model.response.PromptionResponse;
import java.util.Map;
import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.QueryMap;

/**
 * Created by weibo on 15-12-23.
 */
public interface PromptionInterface {

  @POST("social/publish") Call<BaseResponse> postPromption(@Body Map request);

  //公司发布的活动列表&首页推荐列表
  @GET("social/infos") Call<BaseListResponse<Promption>> getPromptionList(@QueryMap Map map);

  @GET("social/infoByid/{promption_id}") Call<PromptionResponse> getPromption(
      @Path("promption_id") int promptionId);

  @GET("social/info") Call<BaseListResponse<Promption>> getBrandPostedPromptionList(
      @QueryMap Map map);

  //公司参加过的活动列表
  @GET("social/Detailinfo/3/{brand_id}/{cp}/{ps}")
  Call<BaseListResponse<Promption>> getBrandJoinedPromptionList(@Path("brand_id") int brandId,
      @Path("cp") int cp, @Path("ps") int ps);

  //我发布的活动列表
  @GET("social/myinfo") Call<BaseListResponse<Promption>> getUserPostedPromptionList(
      @QueryMap Map map);

  //我参加过的活动列表
  @GET("social/myDetailinfo") Call<BaseListResponse<Promption>> getUserJoinedPromptionList(
      @QueryMap Map map);

  @GET("search") Call<BaseListResponse<Promption>> searchPromptionList(@QueryMap Map map);

  @POST("social/join") Call<BaseResponse> join(@Body JoinRequest joinRequest);

  @GET("social/Detailinfo/1/{promption_id}/{cp}/{ps}")
  Call<BaseListResponse<JoinedUser>> getJoinedList(@Path("promption_id") int promptionId,
      @Path("cp") int cp, @Path("ps") int ps);

  @GET("social/JoininfoComplex") Call<JoinInfoResponse> getJoinInfo(@QueryMap Map map);

  @PUT("social/join") Call<BaseResponse> updateJoinState(@Body UpdateJoinStateRequest request);

  @GET("social/complete/Detailinfo") Call<BaseListResponse<JoinedUser>> getApprovedList(
      @QueryMap Map map);

  @PUT("social/complete") Call<BaseResponse> completePromption(
      @Body CompletePromptionRequest request);

  @PUT("social/state") Call<BaseResponse> updatePromptionState(
      @Body UpdatePromptionStateRequest request);
}
