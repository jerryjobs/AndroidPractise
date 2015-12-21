package com.ikaowo.join.modules.user.service;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.common.framework.core.JApplication;
import com.common.framework.network.NetworkCallback;
import com.common.framework.util.JToast;
import com.ikaowo.join.R;
import com.ikaowo.join.common.service.UserService;
import com.ikaowo.join.model.base.BaseResponse;
import com.ikaowo.join.model.request.LoginRequest;
import com.ikaowo.join.model.request.ResetPasswdRequest;
import com.ikaowo.join.model.response.SignupResponse;
import com.ikaowo.join.modules.user.activity.AddBrandActivity;
import com.ikaowo.join.modules.user.activity.BrandListActivity;
import com.ikaowo.join.modules.user.activity.ResetPasswdActivity;
import com.ikaowo.join.modules.user.activity.SigninActivity;
import com.ikaowo.join.modules.user.activity.SignupActivity;
import com.ikaowo.join.network.UserInterface;
import com.ikaowo.join.util.SharedPreferenceHelper;

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
    Call<SignupResponse> call = userNetworkService.signin(request);
    call.enqueue(new NetworkCallback<SignupResponse>() {
      @Override
      public void onSuccess(SignupResponse signupResponse) {
        sharedPreferenceHelper.saveUser(signupResponse.data);
        ((Activity)context).finish();
        JToast.toastShort(context.getString(R.string.login_suc));
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
    call.enqueue(new NetworkCallback<BaseResponse>() {
      @Override
      public void onSuccess(BaseResponse baseResponse) {
        ((Activity)context).finish();
        JToast.toastShort(context.getString(R.string.reset_passwd_suc));
      }
    });
  }


  @Override
  public void onCreate() {

  }

  @Override
  public void onDestroy() {

  }
}
