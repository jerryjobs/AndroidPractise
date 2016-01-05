package com.ikaowo.join.common.service;

import android.content.Context;

import com.common.framework.core.JCommonService;
import com.ikaowo.join.model.UserLoginData;
import com.ikaowo.join.model.response.SignupResponse;

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

  public abstract void doLogin(Context context, String userName, String password, boolean changeTab);

  public abstract void doAfterSignin(Context context, SignupResponse signupResponse, String suc_hint);

  public abstract void resetPasswd(Context context, String userName, String vCode, String password);

  public abstract boolean isLogined();

  public abstract UserLoginData getUser();

  public abstract int getUserCompanyId();

  public abstract int getUserId();

  public abstract void logout(Context context);

  public abstract void updateAvatarInfo(String avatarUrl);

  public abstract boolean authed();

  public abstract void updateLocalUserInfo(boolean passed);

  public abstract void checkLatestUserState(Context context, CheckStateCallback callback);

  public abstract void imChat(Context context, String targetUserWxId);

  public abstract String getLoginedUserName(Context context);

  public interface CheckStateCallback {
    void onPassed();
    void onFailed();
  }
}
