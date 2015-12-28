package com.ikaowo.join.eventbus;

import java.util.Map;

/**
 * Created by weibo on 15-12-28.
 */
public interface GetListCountCallback {
  MapObj getCountMap();

  class MapObj {
    public int index;
    public int count;
  }
}
