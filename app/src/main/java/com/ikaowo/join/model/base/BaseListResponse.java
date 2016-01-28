package com.ikaowo.join.model.base;

import java.util.List;

/**
 * Created by weibo on 14/5/15.
 */
public class BaseListResponse<T> extends BaseResponse {
  public int total;
  public List<T> data;

  @Override public int getTotals() {
    return total;
  }
}
