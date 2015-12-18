package com.ikaowo.join.model.response;

import com.google.gson.annotations.SerializedName;
import com.ikaowo.join.model.Expert;
import com.ikaowo.join.model.base.BaseListResponse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by weibo on 6/5/15.
 */
public class FollowingAndRecExpertResponse extends BaseListResponse {
    public ExtertList pros;

    public Expert get(int pos) {
        return pros.expertList.get(pos);
    }

    public int getSize() {
        return pros.expertList == null ? 0 : pros.expertList.size();
    }

    public class ExtertList implements Serializable {
        @SerializedName("list")
        public List<Expert> expertList;
        @SerializedName("recList")
        public List<Expert> recExpertList;
        public int totals;
    }
}

