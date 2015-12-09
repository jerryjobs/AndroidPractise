package com.ikaowo.marketing.network.bean.base;

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
