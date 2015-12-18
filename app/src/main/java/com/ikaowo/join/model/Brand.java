package com.ikaowo.join.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by weibo on 15-12-18.
 */
public class Brand implements Serializable {
    @SerializedName("company_id")
    public int companyId;

    @SerializedName("company_name")
    public String companyName;

    //规模
    @SerializedName("scale")
    public int scale;

    @SerializedName("company_icon")
    public String companyIcon;

    @SerializedName("brand_name")
    public String brandName;

    @SerializedName("company_py")
    public String brandFirstLetter;

    @SerializedName("summary")
    public String summary;

    @SerializedName("company_hot")
    public int companyHot; //热度

    @SerializedName("brand_logo")
    public String brandLogo;

    public boolean showSection;

    public boolean hideSplit;
}
