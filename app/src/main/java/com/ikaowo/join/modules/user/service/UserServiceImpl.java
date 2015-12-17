package com.ikaowo.join.modules.user.service;

import android.content.Context;
import android.content.Intent;

import com.common.framework.core.JApplication;
import com.ikaowo.join.common.service.UserService;
import com.ikaowo.join.modules.user.activity.AddBrandActivity;
import com.ikaowo.join.modules.user.activity.BrandListActivity;
import com.ikaowo.join.modules.user.activity.ResetPasswdActivity;
import com.ikaowo.join.modules.user.activity.SigninActivity;
import com.ikaowo.join.modules.user.activity.SignupActivity;

/**
 * Created by weibo on 15-12-17.
 */
public class UserServiceImpl extends UserService {
    @Override
    public void goToSignin(Context context) {
        Intent intent = new Intent(context, SigninActivity.class);
        JApplication.getJContext().startActivity(context, intent);
    }

    @Override
    public void resetPassword(Context context) {
        Intent intent = new Intent(context, ResetPasswdActivity.class);
        JApplication.getJContext().startActivity(context, intent);
    }

    @Override
    public void goToSignup(Context context) {
        Intent intent = new Intent(context, SignupActivity.class);
        JApplication.getJContext().startActivity(context, intent);
    }

    @Override
    public void addBrand(Context context) {
        Intent intent = new Intent(context, AddBrandActivity.class);
        JApplication.getJContext().startActivity(context, intent);
    }

    @Override
    public void chooseBrandList(Context context) {
        Intent intent = new Intent(context, BrandListActivity.class);
        JApplication.getJContext().startActivity(context, intent);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDestroy() {

    }
}
