package com.ikaowo.join.modules.user.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.common.framework.core.JApplication;
import com.ikaowo.join.BaseActivity;
import com.ikaowo.join.R;
import com.ikaowo.join.common.service.UserService;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by weibo on 15-12-11.
 */
public class SigninActivity extends BaseActivity {

  private UserService userService = JApplication.getJContext().getServiceByInterface(UserService.class);

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_signin);
    ButterKnife.bind(this);

    toolbar = (Toolbar) findViewById(R.id.toolbar);
    toolbar.setTitle("");
    setSupportActionBar(toolbar);

    displayHomeAsIndicator(R.drawable.nav_ic_close_blue);
  }

  @OnClick(R.id.resetpasswd)
  public void resetPasswd() {
    userService.resetPassword(this);
  }

  @OnClick(R.id.signup_tv)
  public void signup() {
    userService.goToSignup(this);
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
}
