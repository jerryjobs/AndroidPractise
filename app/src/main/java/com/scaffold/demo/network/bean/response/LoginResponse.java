package com.scaffold.demo.network.bean.response;

import com.google.gson.annotations.SerializedName;
import com.scaffold.demo.network.bean.base.BaseResponse;

/**
 * Created by weibo on 15-12-2.
 */
public class LoginResponse extends BaseResponse {
    @SerializedName("u_id")
    public long uid;
}
