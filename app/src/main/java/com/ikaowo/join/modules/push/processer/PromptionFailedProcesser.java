package com.ikaowo.join.modules.push.processer;

import android.content.Context;

import com.ikaowo.join.common.service.PromptionService;
import com.ikaowo.join.util.Constant;

/**
 * Created by weibo on 16-1-12.
 */
public class PromptionFailedProcesser extends PushDataProcesser {

    @Override
    public void openPage(Context context, String target, String targetUrl) {
        PromptionService promptionService = jContext.getServiceByInterface(PromptionService.class);
        promptionService.goToEditPromptionActivity(context, Integer.valueOf(target), Constant.PUSH_PROMPTION_FAILED);
    }
}
