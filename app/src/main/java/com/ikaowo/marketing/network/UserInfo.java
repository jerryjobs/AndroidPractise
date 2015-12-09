package com.ikaowo.marketing.network;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by weibo on 15-12-2.
 */
public class UserInfo implements Serializable {
    @SerializedName("nickname")
    public String nickName;
}
