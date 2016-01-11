package com.ikaowo.join.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * Created by weibo on 15-12-21.
 */
public class UserLoginData implements Serializable {
  @SerializedName("u_id") public int uId;

  @SerializedName("username") public String userName;

  public String icon;

  @SerializedName("nickname") public String nickName;

  @SerializedName("u_company") public int companyId;

  @SerializedName("card") public String cardUrl;

  @SerializedName("u_pos") public String title;

  public String phone;

  @SerializedName("sta") public String state;

  @SerializedName("company_sta") public String companyState;

  @SerializedName("wx") public String wxId;

  @SerializedName("company_info") public Brand brandInfo;

  public String comment;
}
