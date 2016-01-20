package com.ikaowo.join.modules.user.activity;

import android.os.Build;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import com.common.framework.core.JApplication;
import com.common.framework.network.NetworkCallback;
import com.ikaowo.join.R;
import com.ikaowo.join.common.widget.InputMethodLinearLayout;
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

  private int pos = -1;
  private Boolean first = true;//第一次进入页面
  @Override protected void setupView() {
    divider.setVisibility(View.VISIBLE);

    View view = phoneViewStub.inflate();
    phoneViewStub.setVisibility(View.VISIBLE);
    phoneViewHoder = new PhoneViewHolder(view);
    phoneViewHoder.phoneEt.addTextChangedListener(this);
    phoneViewHoder.verifyCodeEt.addTextChangedListener(this);
    phoneViewHoder.passwordEt.addTextChangedListener(this);
    phoneViewHoder.passwordEt.setInputType(
        InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

    verifyCodeHelper =
        new VerifyCodeHelper(this, phoneViewHoder.phoneEt, phoneViewHoder.getVerifyBtn);
    verifyCodeHelper.initVerifyBtn();

    containerLayout.getViewTreeObserver().addOnGlobalLayoutListener(
      new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
          if (phoneViewHoder == null) {
            return;
          }
          //这里必须要设置一下layoutparam 才能滚动正常
          LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(
              ViewGroup.LayoutParams.MATCH_PARENT, phoneViewHoder.phoneRelatedContainerLayout.getMeasuredHeight());
          phoneViewHoder.phoneRelatedContainerLayout.setLayoutParams(llp);

          if (pos == -1) {
            int loc[] = new int[2];
            phoneViewHoder.passwordEt.getLocationInWindow(loc);
            pos = loc[1] - toolbar.getHeight() + toolbar.getHeight() / 3;
          }
          if (Build.VERSION.SDK_INT < 16) {
            containerLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
          } else {
            containerLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
          }
        }
      });

    containerLayout.setOnSizeChangedListenner(
      new InputMethodLinearLayout.OnSizeChangedListenner() {
        @Override public void onSizeChange(boolean paramBoolean, int oldh, int h) {
          //如果第一次进入，获取到高度的变化，也就是键盘的高度。
          if (first) {
            synchronized (first) {
              pos = pos - Math.abs(oldh - h);
              first = false;
            }
          }

          if (phoneViewHoder.phoneEt.isFocused()
              || phoneViewHoder.passwordEt.isFocused()
                || phoneViewHoder.verifyCodeEt.isFocused()) {
            if (paramBoolean) {
              containerLayout.smoothScrollTo(pos, 200);
            } else {
              containerLayout.smoothScrollTo(-pos, 200);
            }
          }
        }
    });

    containerLayout.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        hideInput(SignupActivity.this, toolbar);
      }
    });
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
    return (userPhoneInputed
        && userPaswdInputed
        && verifyInputed
        && passwordInputed
        && super.prepareOptionMenu());
  }

  protected void submit() {
    UserInterface userNetworkService =
        JApplication.getNetworkManager().getServiceByClass(UserInterface.class);
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
      @Override public void onSuccess(SignupResponse signupResponse) {
        userService.doAfterSignin(SignupActivity.this, signupResponse,
            getString(R.string.signup_suc));
      }
    });
  }

  @Override protected String getTag() {
    return "SingupActivity";
  }
}
