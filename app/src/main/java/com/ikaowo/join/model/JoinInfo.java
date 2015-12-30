package com.ikaowo.join.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by leiweibo on 12/31/15.
 */
public class JoinInfo implements Serializable {
  @SerializedName("u_id")
  public int userId;

  @SerializedName("user_icon")
  public String userIcon;

  @SerializedName("u_pos")
  public String title;

  public String phone;

  public String nickname;

  @SerializedName("brand_logo")
  public String brandLogo;

  @SerializedName("brand_name")
  public String brandName;

  public String extra; //加入说明

  @SerializedName("company_id")
  public int brandId;

  @SerializedName("aci_id")
  public int promption_id;

  public List<String> tumblrs = new ArrayList<>();
}
