package com.ikaowo.join.common.service;

import android.content.Context;

import com.common.framework.core.JCommonService;

/**
 * Created by weibo on 15-12-17.
 */
public abstract class UserService extends JCommonService {
    public abstract void goToSignin(Context context);
    public abstract void resetPassword(Context context);
    public abstract void goToSignup(Context context);
    public abstract void addBrand(Context context);
    public abstract void chooseBrandList(Context context);
}
