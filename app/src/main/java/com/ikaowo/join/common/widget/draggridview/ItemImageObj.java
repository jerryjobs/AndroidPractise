package com.ikaowo.join.common.widget.draggridview;

import android.net.Uri;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by weibo on 15-12-22.
 */
public class ItemImageObj implements Serializable {

    public static final int TYPE_ADD = 0, TYPE_NORMAL = 1;

    @SerializedName("u_id")
    public long uId;

    @SerializedName("tbr_id")
    public long thumbId;

    @SerializedName("tbr_img")
    public String thumbImg = "";

    @SerializedName("tbr_txt")
    public String thumbTxt = "";

    @SerializedName("sq")
    public int order;

    public int type = TYPE_NORMAL; // 0: add, 2:normal //数据库对应的2表示活动的照片

    public Uri uri;

    @Override
    public boolean equals(Object o) {
        boolean isEqual = true;
        ItemImageObj itemImageObj = (ItemImageObj) o;
        if (itemImageObj == null) {
            isEqual = false;
        } else {
            isEqual = thumbTxt.equals(itemImageObj.thumbTxt) && thumbImg.equals(itemImageObj.thumbImg);
        }

        return isEqual;
    }
}