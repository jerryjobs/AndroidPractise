package com.ikaowo.join.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by weibo on 15-12-30.
 */
public class JoinedUser implements Serializable {
  public int id;
  @SerializedName("aci_id")
  public int promption_id;
  @SerializedName("u_id")
  public int uId;
  public String comment;
  public String extra;
  public String state;
  @SerializedName("stateCN")
  public String stateDesc;
  @SerializedName("company_name")
  public String companyName;
  @SerializedName("brand_name")
  public String brandName;
  @SerializedName("brand_icon")
  public String brandIcon;
}
