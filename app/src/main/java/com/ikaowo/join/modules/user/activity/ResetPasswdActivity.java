package com.ikaowo.join.modules.user.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.ikaowo.join.BaseActivity;
import com.ikaowo.join.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by weibo on 15-12-17.
 */
public class ResetPasswdActivity extends BaseActivity {

  @Bind(R.id.verify_btn)
  TextView verifyBtn;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_reset_passwd);
    ButterKnife.bind(this);

    toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    toolbar.setTitle(R.string.title_activity_resetpasswd);

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
  }

  @OnClick(R.id.verify_btn)
  public void getVerifyCode() {
    Log.e(getTag(), "Reset passwd clicked");
  }

  @Override
  protected String getTag() {
    return "ResetPasswdActivity";
  }
}
