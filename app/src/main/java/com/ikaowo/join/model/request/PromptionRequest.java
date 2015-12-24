package com.ikaowo.join.model.request;

import com.ikaowo.join.common.widget.draggridview.ItemImageObj;
import com.ikaowo.join.model.base.BaseRequest;

import java.util.List;

/**
 * Created by weibo on 15-12-23.
 */
public class PromptionRequest extends BaseRequest {
  public String aci_name;
  public String aci_date;
  public String aci_content;
  public String aci_notice;
  public int aci_company_id; //活动发起方公司.
  public String aci_icon; //活动icon.
  public String end_date;
  public List<ItemImageObj> aci_tumblrs;
}
