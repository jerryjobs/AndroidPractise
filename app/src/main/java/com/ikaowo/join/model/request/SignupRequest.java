package com.ikaowo.join.model.request;

import com.ikaowo.join.model.BrandInfo;
import com.ikaowo.join.model.base.BaseRequest;

/**
 * Created by weibo on 15-12-21.
 */
public class SignupRequest extends BaseRequest {
  public int u_id;
  public String phone;
  public String nickname;
  public String password;
  public String vcode;
  public String position;
  public String tumblr_icon;
  public int company_id;
  public BrandInfo brand_info;
}