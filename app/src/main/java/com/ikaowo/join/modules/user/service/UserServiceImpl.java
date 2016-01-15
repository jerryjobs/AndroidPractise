package com.ikaowo.join.modules.user.service;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.login.YWLoginState;
import com.common.framework.core.JApplication;
import com.common.framework.core.JDialogHelper;
import com.common.framework.core.JFragmentActivity;
import com.common.framework.network.NetworkCallback;
import com.common.framework.network.NetworkManager;
import com.common.framework.util.JToast;
import com.ikaowo.join.R;
import com.ikaowo.join.common.service.UserService;
import com.ikaowo.join.eventbus.CheckLatestStateCallback;
import com.ikaowo.join.eventbus.ClickTabCallback;
import com.ikaowo.join.eventbus.ClosePageCallback;
import com.ikaowo.join.eventbus.SigninCallback;
import com.ikaowo.join.eventbus.SignoutCallback;
import com.ikaowo.join.eventbus.UpdatedataCallback;
import com.ikaowo.join.im.helper.LoginHelper;
import com.ikaowo.join.model.UserLatestState;
import com.ikaowo.join.model.UserLoginData;
import com.ikaowo.join.model.base.BaseResponse;
import com.ikaowo.join.model.request.CheckStateRequest;
import com.ikaowo.join.model.request.LoginRequest;
import com.ikaowo.join.model.request.ResetPasswdRequest;
import com.ikaowo.join.model.response.CheckStateResponse;
import com.ikaowo.join.model.response.SignupResponse;
import com.ikaowo.join.modules.promption.activity.JoinActivity;
import com.ikaowo.join.modules.user.activity.AddBrandActivity;
import com.ikaowo.join.modules.user.activity.BrandListActivity;
import com.ikaowo.join.modules.user.activity.ReSubmitInfoActivity;
import com.ikaowo.join.modules.user.activity.ResetPasswdActivity;
import com.ikaowo.join.modules.user.activity.SigninActivity;
import com.ikaowo.join.modules.user.activity.SignupActivity;
import com.ikaowo.join.network.KwMarketNetworkCallback;
import com.ikaowo.join.network.UserInterface;
import com.ikaowo.join.util.Constant;
import com.ikaowo.join.util.SharedPreferenceHelper;
import de.greenrobot.event.EventBus;
import retrofit.Call;

/**
 * Created by weibo on 15-12-17.
 */
public class UserServiceImpl extends UserService {

  private SharedPreferenceHelper sharedPreferenceHelper = SharedPreferenceHelper.getInstance();
  private boolean changTab = true;

  @Override public void goToSignin(Context context) {
    Intent intent = new Intent(context, SigninActivity.class);
    intent.putExtra(Constant.CHANGE_TAB, true); //从这里进入的登录页面，登录完成之后，都需要进行tab切换。
    JApplication.getJContext().startActivity(context, intent);
  }

  @Override public void resetPassword(Context context) {
    Intent intent = new Intent(context, ResetPasswdActivity.class);
    JApplication.getJContext().startActivity(context, intent);
  }

  @Override public void goToSignup(Context context) {
    Intent intent = new Intent(context, SignupActivity.class);
    JApplication.getJContext().startActivity(context, intent);
  }

  @Override public void addBrand(Context context) {
    Intent intent = new Intent(context, AddBrandActivity.class);
    JApplication.getJContext().startActivity(context, intent);
  }

  @Override public void chooseBrandList(Context context) {
    Intent intent = new Intent(context, BrandListActivity.class);
    intent.putExtra(CHOOSE, true);
    JApplication.getJContext().startActivity(context, intent);
  }

  @Override public void doLogin(final Context context, final String userName, String password) {
    UserInterface userNetworkService =
        JApplication.getNetworkManager().getServiceByClass(UserInterface.class);
    LoginRequest request = new LoginRequest();
    request.username = userName;
    request.password = password;
    JApplication.getNetworkManager()
        .async(context, Constant.LOGINING, userNetworkService.signin(request),
            new NetworkCallback<SignupResponse>(context) {
              @Override public void onSuccess(SignupResponse signupResponse) {

                sharedPreferenceHelper.saveLoginName(context, userName); //TODO 放到异步里面去做
                doAfterSignin(context, signupResponse, context.getString(R.string.login_suc));
              }
            });
  }

  @Override
  public void doLogin(final Context context, String userName, String password, boolean changeTab) {
    changTab = changeTab;
    doLogin(context, userName, password);
  }

  @Override
  public void doAfterSignin(Context context, SignupResponse signupResponse, String suc_hint) {
    sharedPreferenceHelper.saveUser(signupResponse.data);
    EventBus.getDefault().post(new SigninCallback() {
      @Override public boolean singined() {
        return true;
      }

      @Override public boolean changeTab() {
        return changTab;
      }
    });
    ((Activity) context).finish();
    JToast.toastShort(suc_hint);
  }

  @Override
  public void doAfterResubmit(Context context, UserLoginData userLoginData, String suc_hint) {
    sharedPreferenceHelper.saveUser(userLoginData);
    EventBus.getDefault().post(new ClosePageCallback() {
      @Override public boolean close() {
        return true;
      }
    });

    EventBus.getDefault().post(new UpdatedataCallback() {
      @Override public boolean update() {
        return true;
      }
    });

    ((Activity) context).finish();
    JToast.toastShort(suc_hint);
  }

  @Override
  public void resetPasswd(final Context context, String userName, String vCode, String password) {
    UserInterface userNetworkService =
        JApplication.getNetworkManager().getServiceByClass(UserInterface.class);

    ResetPasswdRequest request = new ResetPasswdRequest();
    request.phone = userName;
    request.vcode = vCode;
    request.password = password;

    Call<BaseResponse> call = userNetworkService.resetPasswd(request);
    JApplication.getNetworkManager()
        .async(context, Constant.PROCESSING, call, new KwMarketNetworkCallback(context) {
          @Override public void onSuccess(Object o) {
            ((Activity) context).finish();
            JToast.toastShort(context.getString(R.string.reset_passwd_suc));
          }
        });
  }

  @Override public boolean isLogined() {
    return sharedPreferenceHelper.isLogined();
  }

  @Override public boolean isAuthed() {
    return isLogined() && (Constant.AUTH_STATE_PASSED.equalsIgnoreCase(getUser().state));
  }

  @Override public boolean isPendingAuthed() {
    return isLogined() && (Constant.AUTH_STATE_PENDING_APPROVE.equalsIgnoreCase(getUser().state));
  }

  @Override public boolean isAuthFailed() {
    return isLogined() && (Constant.AUTH_STATE_FAILED.equalsIgnoreCase(getUser().state));
  }

  @Override public int getUserCompanyId() {
    return sharedPreferenceHelper.getUserCompanyId();
  }

  public UserLoginData getUser() {
    return sharedPreferenceHelper.getUser();
  }

  @Override public int getUserId() {
    return sharedPreferenceHelper.getUserId();
  }

  @Override public void logout(final Context context) {
    sharedPreferenceHelper.clearUser();
    JApplication.getNetworkManager().clearCookieStore();
    LoginHelper.getInstance().getIMKit().getLoginService().logout(new IWxCallback() {
      @Override public void onSuccess(Object... objects) {
        Log.e("IMService", "退出登录成功");
      }

      @Override public void onError(int i, String s) {
        Log.e("IMService", "退出登录失败");
      }

      @Override public void onProgress(int i) {

      }
    });
    new Handler().postDelayed(new Runnable() {
      @Override public void run() {
        //将MineActivity 页面关闭
        EventBus.getDefault().post(new ClosePageCallback() {
          @Override public boolean close() {
            return true;
          }
        });

        EventBus.getDefault().post(new SignoutCallback() {
          @Override public boolean signout() {
            return true;
          }
        });
      }
    }, 300);

    EventBus.getDefault().post(new ClickTabCallback() {
      @Override public int getClickedSys() {
        return 0;
      }
    });

    new Handler().postDelayed(new Runnable() {
      @Override public void run() {
        //将MineActivity 页面关闭
        goToSignin(context);
      }
    }, 500);
  }

  @Override public void updateAvatarInfo(String avatarUrl) {
    UserLoginData user = getUser();
    user.icon = avatarUrl;
    sharedPreferenceHelper.saveUser(user);
  }

  @Override public boolean authed() {
    UserLoginData user = getUser();
    return Constant.AUTH_STATE_PASSED.equalsIgnoreCase(user.state)
        && Constant.AUTH_STATE_PASSED.equalsIgnoreCase(user.companyState);
  }

  @Override public void updateLocalUserInfo(UserLatestState state) {
    UserLoginData user = getUser();
    user.state = state.sta;
    user.comment = state.comment;
    //    user.companyState = passed ? Constant.AUTH_STATE_PASSED : Constant.AUTH_STATE_FAILED;

    sharedPreferenceHelper.saveUser(user);
  }

  @Override
  public void interceptorCheckUserState(final Context context, final AuthedAction authedAction) {
    if (isAuthed()) {
      authedAction.doActionAfterAuthed();
    } else if (isAuthFailed()) {
      checkState(context, authedAction);
    } else if (isPendingAuthed()) {
      checkState(context, authedAction);
    } else {
      goToSignin(context);
    }
  }

  private void checkState(final Context context, final AuthedAction authedAction) {
    checkLatestUserState(context, new CheckStateCallback() {
      @Override public void onProcessing() {
        new JDialogHelper((JFragmentActivity) context).showConfirmDialog(context,
            context.getString(R.string.unauthed_hint), new JDialogHelper.DoAfterClickCallback() {
              @Override public void doAction() {
                if (context instanceof JoinActivity) {
                  ((JoinActivity) context).finish();
                }
              }
            });
      }

      @Override public void onPassed() {
        if (authedAction != null) {
          authedAction.doActionAfterAuthed();
        }
      }

      @Override public void onFailed() {
        new JDialogHelper((JFragmentActivity) context).showConfirmDialog(context,
            context.getString(R.string.auth_failed_hint), new JDialogHelper.DoAfterClickCallback() {
              @Override public void doAction() {
                if (context instanceof JoinActivity) {
                  ((JoinActivity) context).finish();
                }
              }
            });
      }
    });
  }

  private void checkLatestUserState(Context context, final CheckStateCallback callback) {
    NetworkManager networkManager = JApplication.getNetworkManager();
    UserInterface userNetworkService = networkManager.getServiceByClass(UserInterface.class);
    CheckStateRequest request = new CheckStateRequest();
    request.u_id = getUserId();
    final Call<CheckStateResponse> call = userNetworkService.checkLatestState(request);
    networkManager.async(call, new KwMarketNetworkCallback<CheckStateResponse>(context) {
      @Override public void onSuccess(final CheckStateResponse response) {
        if (response == null || response.data == null) {
          return;
        }
        updateLocalUserInfo(response.data);

        EventBus.getDefault().post(new CheckLatestStateCallback() {
          @Override public UserLatestState getLatestState() {
            return response.data;
          }
        });

        if (Constant.AUTH_STATE_PASSED.equalsIgnoreCase(response.data.sta)) {
          callback.onPassed();
        } else if (Constant.AUTH_STATE_PENDING_APPROVE.equalsIgnoreCase(response.data.sta)) {
          callback.onProcessing();
        } else {
          callback.onFailed();
        }
      }
    });
  }

  @Override public void imChat(final Context context, final String targetUserWxId) {
    interceptorCheckUserState(context, new AuthedAction() {
      @Override public void doActionAfterAuthed() {
        String target = targetUserWxId;
        if (loginSuccessed(context)) {
          Intent intent = LoginHelper.getInstance().getIMKit().getChattingActivityIntent(target);
          JApplication.getJContext().startActivity(context, intent);
        }
      }
    });
  }

  private boolean loginSuccessed(Context context) {
    LoginHelper loginHelper = LoginHelper.getInstance();
    YWIMKit imKit = loginHelper.getIMKit();
    if (imKit == null) {
      return false;
    }

    if (imKit.getIMCore().getLoginState().equals(YWLoginState.success)) {
      return true;
    } else if (imKit.getIMCore().getLoginState().equals(YWLoginState.idle)) {
      JToast.toastShort(context.getString(R.string.im_service_logining));
      return false;
    } else if (imKit.getIMCore().getLoginState().equals(YWLoginState.fail)) {
      JToast.toastShort(context.getString(R.string.im_service_failed));
      return false;
    } else if (imKit.getIMCore().getLoginState().equals(YWLoginState.fail)) {
      JToast.toastShort(context.getString(R.string.im_service_disconnected));
      return false;
    }
    return false;
  }

  @Override public String getLoginedUserName(Context context) {
    return sharedPreferenceHelper.getLoginedUserName(context);
  }

  @Override public void reSubmitInfo(Context context) {
    Intent intent = new Intent(context, ReSubmitInfoActivity.class);
    JApplication.getJContext().startActivity(context, intent);
  }

  @Override public void onCreate() {

  }

  @Override public void onDestroy() {

  }
}
