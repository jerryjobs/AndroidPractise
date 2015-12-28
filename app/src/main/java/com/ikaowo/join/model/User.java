package com.ikaowo.join.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by weibo on 15-12-28.
 */
public class User implements Serializable {
  @SerializedName("u_id")
  public int id;
  @SerializedName("u_username")
  public String userName;
  @SerializedName("u_icon")
  public String userIcon;
  @SerializedName("u_nickname")
  public String nickName;
  @SerializedName("u_pos")
  public String title;
  @SerializedName("phone")
  public String phone;
}
