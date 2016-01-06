package com.ikaowo.join;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.common.framework.core.JFragment;
import com.common.framework.core.JFragmentActivity;
import com.umeng.analytics.MobclickAgent;

import java.util.List;


/**
 * 含有Fragment的Activity继承这个，跟BaseActivity区别是
 * onResume和onPause里面的区别不一样。
 * Created by weibo on 15-12-1.
 */
public abstract class BaseFragmentActivity extends JFragmentActivity {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
  }

  public void updateFragment(int fragmentContainerId, BaseFragment fragment) {
    updateFragment(fragmentContainerId, fragment, 0, 0);
  }

  public void updateFragment(int fragmentContainerId, BaseFragment fragment, int aniEnter, int aniExit) {
    FragmentManager manager = getSupportFragmentManager();
    FragmentTransaction transaction = manager.beginTransaction();
    transaction.setCustomAnimations(aniEnter, aniExit);
    List<Fragment> fragmentList = manager.getFragments();
    if (fragmentList != null && fragmentList.size() > 0) {
      for (Fragment f : fragmentList) {
        transaction.hide(f);
      }
    }

    JFragment f = (JFragment) manager.findFragmentByTag(fragment.getPageName());
    if (f != null) {
      if (f.isDetached()) {
        transaction.attach(f);
      } else {
        transaction.show(f);
      }
    } else {
      transaction.replace(fragmentContainerId, fragment, fragment.getPageName());
    }

    transaction.commitAllowingStateLoss();
  }

  @Override
  protected void onResume() {
    super.onResume();
    MobclickAgent.onResume(this);
  }

  @Override
  protected void onPause() {
    super.onPause();
    MobclickAgent.onPause(this);
  }

}
