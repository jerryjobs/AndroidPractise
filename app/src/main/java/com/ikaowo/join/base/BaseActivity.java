package com.ikaowo.join.base;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import com.common.framework.core.JFragmentActivity;
import com.umeng.analytics.MobclickAgent;
import de.greenrobot.event.EventBus;

/**
 * 不含有Fragment的Activity继承这个，跟BaseFragmentActivity区别是
 * onResume和onPause里面的区别不一样。
 * Created by weibo on 15-12-1.
 */
public abstract class BaseActivity extends JFragmentActivity {

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (this instanceof EventBusListener) {
      EventBus.getDefault().register(this);
    }
    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
  }

  @Override protected void onResume() {
    super.onResume();
    MobclickAgent.onResume(this);
    MobclickAgent.onPageStart(getTag());
  }

  @Override protected void onPause() {
    super.onPause();
    MobclickAgent.onPause(this);
    MobclickAgent.onPageEnd(getTag());
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    if (this instanceof EventBusListener) {
      EventBus.getDefault().unregister(this);
    }
  }

  protected void displayHomeAsIndicator(int drawable) {
    ActionBar ab = getSupportActionBar();
    if (ab == null) {
      return;
    }
    ab.setDisplayHomeAsUpEnabled(true);

    if (toolbar != null && drawable > 0) {
      toolbar.setNavigationIcon(drawable);
    }
  }
}
