package com.ikaowo.join.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by weibo on 15-12-21.
 */
public class UserLoginData implements Serializable {
  @SerializedName("u_id")
  public int uId;

  @SerializedName("u_username")
  public String userName;

  @SerializedName("u_nickname")
  public String nickName;

  @SerializedName("u_pos")
  public String title;

  @SerializedName("sta")
  public String status;

  @SerializedName("wx")
  public String wxId;
}
