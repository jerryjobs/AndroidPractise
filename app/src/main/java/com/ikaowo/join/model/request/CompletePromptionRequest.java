package com.ikaowo.join.model.request;

import com.ikaowo.join.model.base.BaseRequest;

import java.util.Set;

/**
 * Created by weibo on 16-1-8.
 */
public class CompletePromptionRequest extends BaseRequest {
    public int aci_id;
    public Set<Integer> u_id;
    public String media_link; //媒体
}
