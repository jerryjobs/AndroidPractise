package com.ikaowo.join.modules.push.processer;

import android.content.Context;

import com.ikaowo.join.common.service.PromptionService;

/**
 * Created by weibo on 16-1-12.
 */
public class JoinNewProcesser extends PushDataProcesser {

    @Override
    public void openPage(Context context, String target, String targetUrl) {
        PromptionService promptionService = jContext.getServiceByInterface(PromptionService.class);
        promptionService.viewJoinedUserList(context, Integer.valueOf(target));
    }
}
