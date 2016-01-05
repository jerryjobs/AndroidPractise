package com.ikaowo.join.model;

import com.google.gson.annotations.SerializedName;
import com.ikaowo.join.common.widget.draggridview.ItemImageObj;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by leiweibo on 12/31/15.
 */
public class JoinInfo implements Serializable {
  @SerializedName("u_id")
  public int userId;

  @SerializedName("aci_u_id")
  public int publishUId;

  @SerializedName("user_icon")
  public String userIcon;

  @SerializedName("u_pos")
  public String title;

  @SerializedName("wx")
  public String wx;

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

  public List<ItemImageObj> tumblrs = new ArrayList<>();
}
