package com.ikaowo.join.modules.promption.service;

import android.content.Context;
import android.content.Intent;

import com.common.framework.core.JApplication;
import com.ikaowo.join.common.service.PromptionService;
import com.ikaowo.join.modules.promption.activity.AddPromptionActivity;
import com.ikaowo.join.modules.promption.activity.JoinDetailActivity;
import com.ikaowo.join.modules.promption.activity.JoinedListActivity;
import com.ikaowo.join.modules.promption.activity.SearchPromptionActivity;
import com.ikaowo.join.util.Constant;

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
  public void viewJoinedUserList(Context context, int promptionId) {
    Intent intent = new Intent(context, JoinedListActivity.class);
    intent.putExtra(Constant.PROMPTION_ID, promptionId);
    JApplication.getJContext().startActivity(context, intent);
  }

  @Override
  public void viewJoinDetail(Context context, int brandId, int promptionId) {
    Intent intent = new Intent(context, JoinDetailActivity.class);
    intent.putExtra(Constant.BRAND_ID, brandId);
    intent.putExtra(Constant.PROMPTION_ID, promptionId);
    JApplication.getJContext().startActivity(context, intent);
  }

  @Override
  public void onCreate() {

  }

  @Override
  public void onDestroy() {

  }
}
