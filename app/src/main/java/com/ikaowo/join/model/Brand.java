package com.ikaowo.join.model;

import java.io.Serializable;

/**
 * Created by weibo on 15-12-18.
 */
public class Brand implements Serializable {
    public int company_id;

    public String company_name;

    //规模
    public int scale;

    public String company_icon;

    public String brand_name;

    public String company_py;

    public String summary;

    public int company_hot; //热度

    public String brand_logo;

    public boolean showSection;

    public boolean hideSplit;
}
