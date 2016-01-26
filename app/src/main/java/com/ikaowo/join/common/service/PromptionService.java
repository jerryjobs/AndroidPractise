package com.ikaowo.join.common.service;

import android.content.Context;
import com.common.framework.core.JCommonService;

/**
 * Created by weibo on 15-12-22.
 */
public abstract class PromptionService extends JCommonService {

  public abstract void goToAddPromotionActivity(Context context);

  public abstract void searchPromptionActivity(Context context);

  public abstract void viewJoinedUserList(Context context, int promptionId);

  public abstract void viewJoinDetail(Context context, int brandId, int promptionId);

  public abstract void goToEditPromptionActivity(Context context, int promptionId);

  //From push or system notification list
  public abstract void goToEditPromptionActivity(Context context, int promptionId, String type);
}
