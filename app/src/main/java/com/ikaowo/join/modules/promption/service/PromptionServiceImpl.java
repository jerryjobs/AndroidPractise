package com.ikaowo.join.modules.promption.service;

import android.content.Context;
import android.content.Intent;

import com.common.framework.core.JApplication;
import com.ikaowo.join.common.service.PromptionService;
import com.ikaowo.join.modules.promption.activity.AddPromptionActivity;
import com.ikaowo.join.modules.promption.activity.SearchPromptionActivity;

/**
 * Created by weibo on 15-12-22.
 */
public class PromptionServiceImpl extends PromptionService {

  @Override
  public void goToAddPromotionActivity(Context context) {
    Intent intent = new Intent(context, AddPromptionActivity.class);
    JApplication.getJContext().startActivity(context, intent);
  }

  @Override
  public void searchPromptionActivity(Context context) {
    Intent intent = new Intent(context, SearchPromptionActivity.class);
    JApplication.getJContext().startActivity(context, intent);
  }

  @Override
  public void onCreate() {

  }

  @Override
  public void onDestroy() {

  }
}
