package com.ikaowo.join.common.service;

import android.content.Context;

import com.common.framework.core.JCommonService;

/**
 * Created by weibo on 15-12-29.
 */
public abstract class MineService extends JCommonService {
    public abstract void viewUserInfo(Context context);

    public abstract void updatePassword(Context context);

    public abstract void viewMyPromption(Context context);
}
