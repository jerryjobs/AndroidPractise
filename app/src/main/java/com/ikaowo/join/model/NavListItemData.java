package com.ikaowo.join.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by lvjunling on 15/10/16.
 */
public class NavListItemData {


    public String types;
    @SerializedName("data")
    public List<NavItem> datas;

    public class NavItem{
        @SerializedName("u_id")
        public int uId;
        @SerializedName("srv_num")
        public int srvNum;
        @SerializedName("nickname")
        public String nickName;
        @SerializedName("full_icon")
        public String fullIcon;
        @SerializedName("pro_des")
        public String proDes;
        @SerializedName("ui_title")
        public String uiTitle;
        @SerializedName("target")
        public String target;
        public String icon;
    }
}
