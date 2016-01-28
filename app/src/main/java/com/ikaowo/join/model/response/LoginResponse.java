package com.ikaowo.join.model.response;

import com.google.gson.annotations.SerializedName;
import com.ikaowo.join.model.base.BaseResponse;

/**
 * Created by weibo on 15-12-2.
 */
public class LoginResponse extends BaseResponse {
  @SerializedName("u_id") public long uid;
}
