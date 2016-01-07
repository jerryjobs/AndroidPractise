package com.ikaowo.join.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by weibo on 16-1-7.
 */
public class EnumData implements Serializable {
  @SerializedName("enum_key")
  public String key;
  @SerializedName("enum_value")
  public String value;
  @SerializedName("enum_type")
  public String type;
}
