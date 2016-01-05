package com.ikaowo.join.modules.user.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.TextView;

import com.common.framework.core.JApplication;
import com.ikaowo.join.BaseEventBusActivity;
import com.ikaowo.join.R;
import com.ikaowo.join.common.service.UserService;
import com.ikaowo.join.eventbus.ClosePageCallback;
import com.ikaowo.join.modules.user.widget.DeletableEditTextView;
import com.ikaowo.join.util.Constant;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by weibo on 15-12-11.
 */
public class SigninActivity extends BaseEventBusActivity implements TextWatcher {

  private UserService userService
          = JApplication.getJContext().getServiceByInterface(UserService.class);
  private String username;
  private String password;
  private boolean changeTab;

  @Bind(R.id.name_et)
  DeletableEditTextView nameEt;
  @Bind(R.id.passwod_et)
  DeletableEditTextView passwordEt;
  @Bind(R.id.login_tv)
  TextView loginTv;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_signin);
    ButterKnife.bind(this);

    toolbar = (Toolbar) findViewById(R.id.toolbar);
    toolbar.setTitle("");
    setSupportActionBar(toolbar);

    displayHomeAsIndicator(R.drawable.nav_ic_close_blue);

    if (getIntent().getExtras() != null) {
      changeTab = getIntent().getExtras().getBoolean(Constant.CHANGE_TAB, false);
    }

    setLoginedUaserName();
    setupView();
  }

  private void setLoginedUaserName() {
    String uname = userService.getLoginedUserName(this);
    if (!TextUtils.isEmpty(uname)) {
      nameEt.setText(uname);
      passwordEt.requestFocus();
    }
  }

  public void setupView() {
    nameEt.setSingleLine();

    loginTv.setEnabled(false);
    loginTv.setAlpha(0.3f);

    passwordEt.setSingleLine();
    passwordEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

    nameEt.addTextChangedListener(this);
    passwordEt.addTextChangedListener(this);

  }

  @OnClick(R.id.resetpasswd)
  public void resetPasswd() {
    userService.resetPassword(this);
  }

  @OnClick(R.id.signup_tv)
  public void signup() {
    userService.goToSignup(this);
  }

  public void onEvent(ClosePageCallback callback) {
    if (callback.close()) {
      finish();
    }
  }

  @OnClick(R.id.login_tv)
  public void login() {
    userService.doLogin(this, username, password, changeTab);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int itemId = item.getItemId();
    switch (itemId) {
      case android.R.id.home:
        finish();
        break;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  protected String getTag() {
    return "SigninActivity";
  }

  @Override
  public void beforeTextChanged(CharSequence s, int start, int count, int after) {

  }

  @Override
  public void onTextChanged(CharSequence s, int start, int before, int count) {

  }

  @Override
  public void afterTextChanged(Editable s) {
    username = nameEt.getText().toString().trim();
    password = passwordEt.getText().toString().trim();
    boolean userNameInputed = !(TextUtils.isEmpty(username));
    boolean passwordInputed = !(TextUtils.isEmpty(password));
    boolean enable = userNameInputed && passwordInputed;
    loginTv.setEnabled(enable);
    if (enable) {
      loginTv.setAlpha(1f);
    } else {
      loginTv.setAlpha(0.3f);
    }
  }
}
