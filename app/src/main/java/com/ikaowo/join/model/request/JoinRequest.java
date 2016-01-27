package com.ikaowo.join.model.request;

import com.ikaowo.join.common.widget.draggridview.ItemImageObj;
import com.ikaowo.join.model.base.BaseRequest;

import java.util.List;

/**
 * Created by weibo on 15-12-29.
 */
public class JoinRequest extends BaseRequest {
    public int aci_id;
    public int u_id;
    public String comment;
    public List<ItemImageObj> aci_tumblrs;
}
