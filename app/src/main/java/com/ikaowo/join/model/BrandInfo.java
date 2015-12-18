package com.ikaowo.join.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by weibo on 15-12-17.
 */
public class BrandInfo implements Serializable {
    @SerializedName("brand_name")
    public String brandName;

    @SerializedName("brand_logo")
    public String brandLogo;

    @SerializedName("company_icon")
    public String companyIcon; // 营业执照字段
}
