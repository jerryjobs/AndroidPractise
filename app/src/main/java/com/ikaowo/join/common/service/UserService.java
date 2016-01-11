package com.ikaowo.join.common.service;

import android.content.Context;
import com.common.framework.core.JCommonService;
import com.ikaowo.join.model.UserLatestState;
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

  public abstract void doLogin(Context context, String userName, String password,
      boolean changeTab);

  public abstract void doAfterSignin(Context context, SignupResponse signupResponse,
      String suc_hint);

  public abstract void doAfterResubmit(Context context, UserLoginData userLoginData,
      String suc_hint);

  public abstract void resetPasswd(Context context, String userName, String vCode, String password);

  public abstract boolean isLogined();

  public abstract boolean isAuthed();

  public abstract boolean isPendingAuthed();

  public abstract boolean isAuthFailed();

  public abstract UserLoginData getUser();

  public abstract int getUserCompanyId();

  public abstract int getUserId();

  public abstract void logout(Context context);

  public abstract void updateAvatarInfo(String avatarUrl);

  public abstract boolean authed();

  public abstract void updateLocalUserInfo(UserLatestState state);

  public abstract void interceptorCheckUserState(Context context, AuthedAction authedAction);

  public abstract void imChat(Context context, String targetUserWxId);

  public abstract String getLoginedUserName(Context context);

  public abstract void reSubmitInfo(Context context);

  public interface CheckStateCallback {
    void onProcessing();

    void onPassed();

    void onFailed();
  }

  public interface AuthedAction {
    void doActionAfterAuthed();
  }
}
