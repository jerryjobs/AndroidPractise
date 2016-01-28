package com.ikaowo.join.network;

import com.ikaowo.join.model.response.TokenResponse;
import java.util.Map;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.QueryMap;

/**
 * Created by weibo on 15-12-16.
 */
public interface QiniuInterface {
  @GET("file/token") Call<TokenResponse> getQiniuToken(@QueryMap Map map);
}
