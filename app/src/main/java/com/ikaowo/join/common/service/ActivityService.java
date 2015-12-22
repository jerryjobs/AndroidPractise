package com.ikaowo.join.common.service;


import android.content.Context;

import com.common.framework.core.JCommonService;

/**
 * Created by weibo on 15-12-22.
 */
public abstract class ActivityService extends JCommonService {

  protected abstract void goToAddPromotionActivity(Context context);
}
