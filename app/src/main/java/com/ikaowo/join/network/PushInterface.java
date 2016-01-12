package com.ikaowo.join.network;

import com.ikaowo.join.model.PushClientIdRequest;
import com.ikaowo.join.model.base.BaseResponse;
import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by weibo on 16-1-12.
 */
public interface PushInterface {
  @POST("user/getui") Call<BaseResponse> updatePushClientId(@Body  PushClientIdRequest request);

}
