package com.ikaowo.join.modules.user.service;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.alibaba.mobileim.IYWLoginService;
import com.alibaba.mobileim.YWLoginParam;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.common.framework.core.JApplication;
import com.common.framework.network.NetworkCallback;
import com.common.framework.util.JToast;
import com.ikaowo.join.R;
import com.ikaowo.join.common.service.UserService;
import com.ikaowo.join.eventbus.ClickTabCallback;
import com.ikaowo.join.eventbus.ClosePageCallback;
import com.ikaowo.join.eventbus.SigninCallback;
import com.ikaowo.join.eventbus.SignoutCallback;
import com.ikaowo.join.im.helper.LoginHelper;
import com.ikaowo.join.model.UserLoginData;
import com.ikaowo.join.model.base.BaseResponse;
import com.ikaowo.join.model.request.LoginRequest;
import com.ikaowo.join.model.request.ResetPasswdRequest;
import com.ikaowo.join.model.response.SignupResponse;
import com.ikaowo.join.modules.user.activity.AddBrandActivity;
import com.ikaowo.join.modules.user.activity.BrandListActivity;
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
    intent.putExtra(CHOOSE, true);
    JApplication.getJContext().startActivity(context, intent);
  }

  @Override
  public void doLogin(final Context context, String userName, String password) {
    UserInterface userNetworkService
      = JApplication.getNetworkManager().getServiceByClass(UserInterface.class);
    LoginRequest request = new LoginRequest();
    request.username = userName;
    request.password = password;
    JApplication.getNetworkManager().async(context, Constant.LOGINING,
      userNetworkService.signin(request),
      new NetworkCallback<SignupResponse>(context) {
        @Override
        public void onSuccess(SignupResponse signupResponse) {
          sharedPreferenceHelper.saveUser(signupResponse.data);
          initWxService();
          EventBus.getDefault().post(new SigninCallback() {
            @Override
            public boolean singined() {
              return true;
            }
          });
          ((Activity) context).finish();
          JToast.toastShort(context.getString(R.string.login_suc));
        }
      });
  }


  private void initWxService() {
    String userid = "testpro22";
    String password = "taobao1234";

    LoginHelper loginHelper = LoginHelper.getInstance();
    IYWLoginService loginService = loginHelper.getIMKit().getLoginService();
    YWLoginParam loginParam = YWLoginParam.createLoginParam(userid, password);
    loginService.login(loginParam, new IWxCallback() {

      @Override
      public void onSuccess(Object... objects) {
        Log.e("weiboooo", "login success...");
      }

      @Override
      public void onError(int i, String s) {
        Log.e("weiboooo", "login failed...");
      }

      @Override
      public void onProgress(int i) {

      }
    });
  }

  @Override
  public void resetPasswd(final Context context, String userName, String vCode, String password) {
    UserInterface userNetworkService
      = JApplication.getNetworkManager().getServiceByClass(UserInterface.class);

    ResetPasswdRequest request = new ResetPasswdRequest();
    request.phone = userName;
    request.vcode = vCode;
    request.password = password;

    Call<BaseResponse> call = userNetworkService.resetPasswd(request);
    JApplication.getNetworkManager().async(context, Constant.PROCESSING, call, new KwMarketNetworkCallback(context) {
      @Override
      public void onSuccess(Object o) {
        ((Activity) context).finish();
        JToast.toastShort(context.getString(R.string.reset_passwd_suc));
      }
    });
  }

  @Override
  public boolean isLogined() {
    return sharedPreferenceHelper.isLogined();
  }

  @Override
  public int getUserCompanyId() {
    return sharedPreferenceHelper.getUserCompanyId();
  }

  public UserLoginData getUser() {
    return sharedPreferenceHelper.getUser();
  }

  @Override
  public int getUserId() {
    return sharedPreferenceHelper.getUserId();
  }

  @Override
  public void logout(final Context context) {
    sharedPreferenceHelper.clearUser();
    LoginHelper.getInstance().getIMKit().getLoginService().logout(new IWxCallback() {
      @Override
      public void onSuccess(Object... objects) {
        Log.e("IMService", "退出登录成功");
      }

      @Override
      public void onError(int i, String s) {
        Log.e("IMService", "退出登录失败");
      }

      @Override
      public void onProgress(int i) {

      }
    });
    JToast.toastShort("退出登录成功...");
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        //将MineActivity 页面关闭
        EventBus.getDefault().post(new ClosePageCallback() {
          @Override
          public boolean close() {
            return true;
          }
        });

        EventBus.getDefault().post(new SignoutCallback() {
          @Override
          public boolean signout() {
            return true;
          }
        });
      }
    }, 600);

    EventBus.getDefault().post(new ClickTabCallback() {
      @Override
      public int getClickedSys() {
        return 0;
      }
    });

    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        //将MineActivity 页面关闭
        goToSignin(context);
      }
    }, 500);
  }

  @Override
  public void onCreate() {

  }

  @Override
  public void onDestroy() {

  }
}
