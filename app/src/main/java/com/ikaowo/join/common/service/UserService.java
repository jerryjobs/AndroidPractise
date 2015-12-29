package com.ikaowo.join.common.service;

import android.content.Context;

import com.common.framework.core.JCommonService;
import com.ikaowo.join.model.UserLoginData;

/**
 * Created by weibo on 15-12-17.
 */
public abstract class UserService extends JCommonService {

  public static final String CHOOSE = "action_choose";

  public abstract void goToSignin(Context context);

  public abstract void resetPassword(Context context);

  public abstract void goToSignup(Context context);

  public abstract void addBrand(Context context);

  public abstract void chooseBrandList(Context context);

  public abstract void doLogin(Context context, String userName, String password);

  public abstract void resetPasswd(Context context, String userName, String vCode, String password);

  public abstract boolean isLogined();

  public abstract UserLoginData getUser();

  public abstract int getUserCompanyId();

  public abstract int getUserId();

  public abstract void logout(Context context);
}
