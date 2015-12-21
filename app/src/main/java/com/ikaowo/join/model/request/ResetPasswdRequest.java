package com.ikaowo.join.model.request;

import com.ikaowo.join.model.base.BaseRequest;

/**
 * Created by weibo on 15-12-21.
 */
public class ResetPasswdRequest extends BaseRequest {
  public String phone;
  public String vcode;
  public String password;
}
