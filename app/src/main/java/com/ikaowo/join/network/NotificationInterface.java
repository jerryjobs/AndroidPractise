package com.ikaowo.join.network;

import com.ikaowo.join.model.Notification;
import com.ikaowo.join.model.base.BaseListResponse;
import com.ikaowo.join.model.base.BaseResponse;
import java.util.Map;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.PUT;
import retrofit.http.QueryMap;

/**
 * Created by weibo on 16-1-11.
 */
public interface NotificationInterface {
  @GET("message") Call<BaseListResponse<Notification>> getNoticeList(
      @QueryMap Map<String, String> map);

  @PUT("message") Call<BaseResponse> markAsRed(@QueryMap Map map);
}
