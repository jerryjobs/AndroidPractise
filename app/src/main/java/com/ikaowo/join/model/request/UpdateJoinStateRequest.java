package com.ikaowo.join.model.request;

import com.ikaowo.join.model.base.BaseRequest;
import java.util.List;

/**
 * Created by leiweibo on 1/5/16.
 */
public class UpdateJoinStateRequest extends BaseRequest {
  public int aci_id;
  public List<Integer> u_id;
  public String u_act;
}
