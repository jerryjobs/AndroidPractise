package com.ikaowo.join.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by weibo on 16-1-11.
 */
public class Notification implements Serializable {
  @SerializedName("ntc_id")
  public int id;
  @SerializedName("is_read")
  public boolean isRead;
  @SerializedName("nt_title")
  public String title;
  @SerializedName("nt_time")
  public String time;
  @SerializedName("nt_contents")
  public String content;

}
