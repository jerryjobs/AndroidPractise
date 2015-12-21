package com.ikaowo.join.model.base;

import com.common.framework.model.JResponse;
import com.google.gson.annotations.SerializedName;

/**
 * Created by weibo on 30/4/15.
 */
public class BaseResponse extends JResponse {
  public int status;
  public String msg;
  public Object err;

  @SerializedName("uid")
  public String uid;

  @Override
  public int getTotals() {
    return 0;
  }
}
