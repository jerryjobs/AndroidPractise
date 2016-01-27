package com.ikaowo.join.modules.push.processer;

import android.content.Context;

import com.ikaowo.join.common.service.MineService;

/**
 * Created by weibo on 16-1-12.
 */
public class AuthSucProcesser extends PushDataProcesser {

    @Override
    public void openPage(Context context, String target, String targetUrl) {
        MineService mineService = jContext.getServiceByInterface(MineService.class);
        mineService.viewUserInfo(context);
    }
}
