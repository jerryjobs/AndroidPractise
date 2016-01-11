package com.ikaowo.join.modules.brand.service;

import android.content.Context;
import android.content.Intent;
import com.ikaowo.join.common.service.BrandService;
import com.ikaowo.join.modules.brand.activity.BrandDetailActivity;
import com.ikaowo.join.util.Constant;

/**
 * Created by weibo on 15-12-28.
 */
public class BrandServiceImpl extends BrandService {

  @Override public void viewBrandDetail(Context context, int brandId) {
    Intent intent = new Intent(context, BrandDetailActivity.class);
    intent.putExtra(Constant.BRAND_ID, brandId);
    context.startActivity(intent);
  }
}
