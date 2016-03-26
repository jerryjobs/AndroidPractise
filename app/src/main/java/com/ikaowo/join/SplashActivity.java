package com.ikaowo.join;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.common.framework.core.JApplication;
import com.ikaowo.join.base.BaseActivity;

/**
 * Created by weibo on 16-2-16.
 */
public class SplashActivity extends BaseActivity {
  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    new Handler().postDelayed(new Runnable() {
      @Override public void run() {
        Intent intent = new Intent(SplashActivity.this, MainTabActivity.class);
        JApplication.getJContext().startActivity(SplashActivity.this, intent);
        finish();
      }
    }, 3000);
  }

  @Override protected String getTag() {
    return "SplashActivity";
  }
}
