package com.ikaowo.marketing.network.bean.response;

import com.google.gson.annotations.SerializedName;
import com.ikaowo.marketing.network.bean.base.BaseResponse;

/**
 * Created by weibo on 15-12-2.
 */
public class LoginResponse extends BaseResponse {
    @SerializedName("u_id")
    public long uid;
}
