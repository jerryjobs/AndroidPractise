package com.ikaowo.join.model.base;

/**
 * Created by weibo on 14/5/15.
 */
public class BaseListResponse extends BaseResponse {
    public int totals;

    @Override
    public int getTotals() {
        return totals;
    }
}
