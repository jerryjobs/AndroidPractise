package com.ikaowo.join.modules.push.processer;

import android.content.Context;
import com.ikaowo.join.common.service.BrandService;

/**
 * Created by weibo on 16-1-12.
 */
public class JoinedProcesser extends PushDataProcesser {

  @Override public void action(Context context, String target) {
    BrandService brandService = jContext.getServiceByInterface(BrandService.class);
    brandService.viewBrandDetail(context, Integer.valueOf(target));
  }
}
