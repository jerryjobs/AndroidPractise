package com.ikaowo.join.modules.push.model;

import java.io.Serializable;

/**
 * Created by weibo on 16-1-12.
 */
public class Push implements Serializable {
  public int id; //notification id;
  public String type;
  public String targetId;
  public String targetUrl;
}
