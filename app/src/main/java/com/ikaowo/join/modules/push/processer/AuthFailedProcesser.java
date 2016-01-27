package com.ikaowo.join.modules.push.processer;

import android.content.Context;

import com.ikaowo.join.common.service.UserService;

/**
 * Created by weibo on 16-1-12.
 */
public class AuthFailedProcesser extends PushDataProcesser {

    @Override
    public void openPage(Context context, String target, String targetUrl) {
        UserService userService = jContext.getServiceByInterface(UserService.class);
        userService.reSubmitInfo(context, true);
    }
}
