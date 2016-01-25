package com.ikaowo.join.modules.user.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.LinearLayout;
import butterknife.Bind;
import com.common.framework.core.JApplication;
import com.common.framework.network.NetworkCallback;
import com.common.framework.network.NetworkManager;
import com.ikaowo.join.R;
import com.ikaowo.join.common.widget.ErrorHintLayout;
import com.ikaowo.join.eventbus.CheckLatestStateCallback;
import com.ikaowo.join.model.BrandInfo;
import com.ikaowo.join.model.UserLatestState;
import com.ikaowo.join.model.UserLoginData;
import com.ikaowo.join.model.request.CheckStateRequest;
import com.ikaowo.join.model.request.SignupRequest;
import com.ikaowo.join.model.response.CheckStateResponse;
import com.ikaowo.join.model.response.SignupResponse;
import com.ikaowo.join.network.KwMarketNetworkCallback;
import com.ikaowo.join.network.UserInterface;
import com.ikaowo.join.util.Constant;
import de.greenrobot.event.EventBus;
import retrofit.Call;

/**
 * Created by leiweibo on 1/9/16.
 */
public class ReSubmitInfoActivity extends BaseUserInputActivity {
  @Bind(R.id.container) public LinearLayout containerLayout;
  UserLoginData user;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    boolean fromPush = getIntent().getExtras() == null ? false : getIntent().getExtras().getBoolean(Constant.NEED_RETRIEVE_LATEST_STATE, false);
    if (fromPush) {
      NetworkManager networkManager = JApplication.getNetworkManager();
      UserInterface userNetworkService = networkManager.getServiceByClass(UserInterface.class);
      CheckStateRequest request = new CheckStateRequest();
      request.u_id = userService.getUserId();
      Call<CheckStateResponse> call = userNetworkService.checkLatestState(request);
      networkManager.async(this, Constant.DATAGETTING, call,
        new KwMarketNetworkCallback<CheckStateResponse>(this) {
          @Override public void onSuccess(final CheckStateResponse stateResponse) {
            if (stateResponse != null && stateResponse.data != null) {
              userService.updateLocalUserInfo(stateResponse.data);
              EventBus.getDefault().post(new CheckLatestStateCallback() {
                @Override public UserLatestState getLatestState() {
                  return stateResponse.data;
                }
              });
              user = userService.getUser();
              user.state = stateResponse.data.sta;
              user.comment = stateResponse.data.comment;
              setupDate(user);
            }
          }
        });
    } else {
      user = userService.getUser();
      setupDate(user);
    }
  }

  private void setupDate(UserLoginData user) {
    if (user.state.equalsIgnoreCase(Constant.AUTH_STATE_FAILED) && !TextUtils.isEmpty(
        user.comment)) {
      ErrorHintLayout errHintLayout = new ErrorHintLayout(this);
      errHintLayout.setText(user.comment);
      containerLayout.addView(errHintLayout, 0);
    }

    brandNameEt.setText(user.brandInfo.brand_name);
    userNameEt.setText(user.nickName);
    userNameEt.setSelection(user.nickName.length());
    userTitleEt.setText(user.title);
    choosedBrand = user.brandInfo;
    userCardUrl = user.cardUrl;
    if (TextUtils.isEmpty(userCardUrl)) {
      userCardIv.setImageResource(R.drawable.register_uppic);
    } else {
      JApplication.getImageLoader()
          .loadImage(userCardIv, userCardUrl, width, width, R.drawable.brand_icon_default);
    }
  }

  @Override protected void submit() {
    UserInterface userNetworkService =
        JApplication.getNetworkManager().getServiceByClass(UserInterface.class);
    SignupRequest request = new SignupRequest();
    request.u_id = userService.getUserId();
    if (choosedBrand.company_id > 0) {
      request.company_id = choosedBrand.company_id;
    } else {
      BrandInfo brandInfo = new BrandInfo();
      brandInfo.brand_logo = choosedBrand.brand_logo;
      brandInfo.brand_name = choosedBrand.brand_name;
      brandInfo.company_icon = choosedBrand.company_icon; //营业执照
      request.brand_info = brandInfo;
    }
    request.nickname = userName;
    request.position = userTitle;
    request.tumblr_icon = userCardUrl;
    Call<SignupResponse> call = userNetworkService.signup(request);
    call.enqueue(new NetworkCallback<SignupResponse>(ReSubmitInfoActivity.this) {
      @Override public void onSuccess(SignupResponse signupResponse) {
        user.nickName = userName;
        user.title = userTitle;
        user.cardUrl = userCardUrl;
        userService.doAfterResubmit(ReSubmitInfoActivity.this, user,
            getString(R.string.resubmit_suc));
      }
    });
  }

  @Override protected String getTag() {
    return "ReSubmitInfoActivity";
  }
}
