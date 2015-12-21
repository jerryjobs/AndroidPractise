package com.ikaowo.join.model.base;

/**
 * Created by weibo on 14/5/15.
 */
public class BaseListResponse extends BaseResponse {
  public int total;

  @Override
  public int getTotals() {
    return total;
  }
}
