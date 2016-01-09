package com.ikaowo.join.modules.user.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.LinearLayout;

import com.common.framework.core.JApplication;
import com.common.framework.network.NetworkCallback;
import com.ikaowo.join.R;
import com.ikaowo.join.common.widget.ErrorHintLayout;
import com.ikaowo.join.model.BrandInfo;
import com.ikaowo.join.model.UserLoginData;
import com.ikaowo.join.model.request.SignupRequest;
import com.ikaowo.join.model.response.SignupResponse;
import com.ikaowo.join.network.UserInterface;

import butterknife.Bind;
import retrofit.Call;

/**
 * Created by leiweibo on 1/9/16.
 */
public class ReSubmitInfoActivity extends BaseUserInputActivity {
  UserLoginData user;

  @Bind(R.id.container)
  public LinearLayout containerLayout;
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    ErrorHintLayout errHintLayout = new ErrorHintLayout(this);
    errHintLayout.setText("这里是错误提示，后台暂时没有返回，所以不显示");
    containerLayout.addView(errHintLayout, 0);

    user = userService.getUser();
    brandNameEt.setText(user.brandInfo.brand_name);
    userNameEt.setText(user.nickName);
    userTitleEt.setText(user.title);
    choosedBrand = user.brandInfo;
    if (TextUtils.isEmpty(user.cardUrl)) {
      userCardIv.setImageResource(R.drawable.register_uppic);
    } else {
      JApplication.getImageLoader().loadImage(userCardIv, user.cardUrl, width, width, R.drawable.brand_icon_default);
    }
  }

  @Override
  protected void submit() {
    UserInterface userNetworkService
      = JApplication.getNetworkManager().getServiceByClass(UserInterface.class);
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
      @Override
      public void onSuccess(SignupResponse signupResponse) {
        user.nickName = userName;
        user.title = userTitle;
        user.cardUrl = userCardUrl;
        userService.doAfterResubmit(ReSubmitInfoActivity.this, user, getString(R.string.resubmit_suc));
      }
    });
  }

  @Override
  protected String getTag() {
    return "ReSubmitInfoActivity";
  }
}
