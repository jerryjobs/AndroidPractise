package com.ikaowo.join.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by weibo on 6/5/15.
 */
public class Expert implements Serializable {

  @SerializedName("u_id")
  public long uId;
  @SerializedName("u_nickname")
  public String nickName;

  public String descript;

  public String des2;

  @SerializedName("u_ated_number")
  public int followerAcct;

  @SerializedName("u_fraction")
  public int rating;

  @SerializedName("u_price")
  public String price;

  @SerializedName("al_price")
  public String askPrice;

  //关注专家列表返回这个
  @SerializedName("ui_summary")
  public String uiSummary;

  public String summary;

  @SerializedName("nxat_sta")
  public String isFollowed;

  @SerializedName("ui_icon")
  public String uiIcon;

  @SerializedName("title_page")
  public String titlePage;

  @SerializedName("backcover")
  public String backCover;

  public int star;

  @SerializedName("ui_title")
  public String uiTitle;//头衔

  @SerializedName("avg_coin")
  public String avgCount;

  @SerializedName("srv_num")
  public int consultNum;

  public List<String> tags;

  public boolean showFollowingSection, showRecSection;
}
