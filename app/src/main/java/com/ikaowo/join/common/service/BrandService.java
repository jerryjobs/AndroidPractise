package com.ikaowo.join.common.service;

import android.content.Context;

import com.common.framework.core.JCommonService;

/**
 * Created by weibo on 15-12-28.
 */
public abstract class BrandService extends JCommonService {

    protected final String BRAND_ID = "brand_id";

    public abstract void viewBrandDetail(Context context, int brandId);

    public abstract void viewBrandIntroduce(Context context, String content);

    @Override
    public void onCreate() {

    }

    @Override
    public void onDestroy() {

    }
}
