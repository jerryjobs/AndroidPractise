package com.ikaowo.join.model;

import com.google.gson.annotations.SerializedName;
import com.ikaowo.join.common.widget.draggridview.ItemImageObj;

import java.io.Serializable;
import java.util.List;

/**
 * Created by weibo on 15-12-24.
 */
public class Promption implements Serializable {
  @SerializedName("aci_id")
  public int id;
  @SerializedName("aci_name")
  public String title;
  @SerializedName("aci_content")
  public String content;
  @SerializedName("aci_company_id")
  public int companyId;
  @SerializedName("aci_limit_head")
  public int limitCount;
  @SerializedName("aci_level")
  public int level;
  @SerializedName("aci_current_head")
  public int currentJoinCount;
  @SerializedName("aci_address")
  public String address;
  @SerializedName("create_date")
  public String createDate;
  @SerializedName("end_date")
  public String endDate;
  @SerializedName("state")
  public String state;
  @SerializedName("stateCN")
  public String stateDesc;
  @SerializedName("aci_icon")
  public String background; // 地址
  @SerializedName("aci_notice")
  public String note;
  @SerializedName("aci_date")
  public String date;
  @SerializedName("aci_u_id")
  public int publishUid;
  @SerializedName("company_name")
  public String companyName;
  @SerializedName("aci_tumblrs")
  public List<ItemImageObj> aci_tumblrs;
}
