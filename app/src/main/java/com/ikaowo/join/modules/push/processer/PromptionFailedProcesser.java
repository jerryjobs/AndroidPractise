package com.ikaowo.join.modules.push.processer;

import android.content.Context;
import com.ikaowo.join.common.service.PromptionService;

/**
 * Created by weibo on 16-1-12.
 */
public class PromptionFailedProcesser extends PushDataProcesser {

  @Override public void action(Context context, String target) {
    PromptionService promptionService = jContext.getServiceByInterface(PromptionService.class);
    promptionService.goToEditPromptionActivity(context, Integer.valueOf(target));
  }
}
