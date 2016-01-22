package com.ikaowo.join.network;

import java.io.Serializable;

/**
 * Created by leiweibo on 1/22/16.
 */
public class UpdatePromptionStateRequest implements Serializable {
  public int aci_id;
  public String u_act;
  public String comment;
}
