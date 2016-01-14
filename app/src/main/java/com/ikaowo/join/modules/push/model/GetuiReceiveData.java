package com.ikaowo.join.modules.push.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * Created by weibo on 16-1-12.
 */
public class GetuiReceiveData implements Serializable {
  @SerializedName("id") public int ntId;
  @SerializedName("title") public String title;
  @SerializedName("contents") public String content;
  @SerializedName("type") public String type;
  @SerializedName("rsId") public String rsId;
}
