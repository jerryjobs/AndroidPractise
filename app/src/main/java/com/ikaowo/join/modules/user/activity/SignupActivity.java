package com.ikaowo.join.modules.user.activity;

import android.text.InputType;
import android.text.TextUtils;
import android.view.View;

import com.common.framework.core.JApplication;
import com.common.framework.network.NetworkCallback;
import com.ikaowo.join.R;
import com.ikaowo.join.model.BrandInfo;
import com.ikaowo.join.model.request.SignupRequest;
import com.ikaowo.join.model.response.SignupResponse;
import com.ikaowo.join.network.UserInterface;
import com.ikaowo.join.util.VerifyCodeHelper;

import retrofit.Call;

/**
 * Created by weibo on 15-12-11.
 */
public class SignupActivity extends BaseUserInputActivity {
  @Override
  protected void setupView() {
    divider.setVisibility(View.VISIBLE);

    View view = phoneViewStub.inflate();
    phoneViewStub.setVisibility(View.VISIBLE);
    phoneViewHoder = new PhoneViewHolder(view);
    phoneViewHoder.phoneEt.addTextChangedListener(this);
    phoneViewHoder.verifyCodeEt.addTextChangedListener(this);
    phoneViewHoder.passwordEt.addTextChangedListener(this);
    phoneViewHoder.passwordEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

    verifyCodeHelper = new VerifyCodeHelper(this, phoneViewHoder.phoneEt, phoneViewHoder.getVerifyBtn);
    verifyCodeHelper.initVerifyBtn();

    super.setupView();
  }

  protected boolean prepareOptionMenu() {
    phone = phoneViewHoder.phoneEt.getText().toString().trim();
    password = phoneViewHoder.passwordEt.getText().toString().trim();
    verifyCode = phoneViewHoder.verifyCodeEt.getText().toString().trim();
    password = phoneViewHoder.passwordEt.getText().toString().trim();

    boolean userPhoneInputed = !TextUtils.isEmpty(phone);
    boolean userPaswdInputed = !TextUtils.isEmpty(password);
    boolean verifyInputed = !TextUtils.isEmpty(verifyCode);
    boolean passwordInputed = !TextUtils.isEmpty(password) && password.length() >= 6;
    return (userPhoneInputed && userPaswdInputed
      && verifyInputed && passwordInputed && super.prepareOptionMenu());
  }

  protected void submit() {
    UserInterface userNetworkService
      = JApplication.getNetworkManager().getServiceByClass(UserInterface.class);
    SignupRequest request = new SignupRequest();
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
    request.phone = phone;
    request.vcode = verifyCode;
    request.password = password;
    Call<SignupResponse> call = userNetworkService.signup(request);
    call.enqueue(new NetworkCallback<SignupResponse>(SignupActivity.this) {
      @Override
      public void onSuccess(SignupResponse signupResponse) {
        userService.doAfterSignin(SignupActivity.this, signupResponse, getString(R.string.signup_suc));
      }
    });
  }

  @Override
  protected String getTag() {
    return "SingupActivity";
  }
}
