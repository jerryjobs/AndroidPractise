package com.ikaowo.join.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import com.common.framework.core.JFragment;
import com.umeng.analytics.MobclickAgent;
import de.greenrobot.event.EventBus;

/**
 * Created by weibo on 15-12-8.
 */
public abstract class BaseFragment extends JFragment {

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (this instanceof EventBusListener) {
      EventBus.getDefault().register(this);
    }
  }

  @Override public void onDestroy() {
    super.onDestroy();
    if (this instanceof EventBusListener) {
      EventBus.getDefault().unregister(this);
    }
  }

  protected void showInputMethod(final View view) {
    view.postDelayed(new Runnable() {
      @Override public void run() {
        InputMethodManager imm =
            (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
          imm.showSoftInput(view, 0);
        }
      }
    }, 200);
  }

  public abstract String getPageName();

  @Override public void onResume() {
    super.onResume();
    MobclickAgent.onPageStart(getPageName());
  }

  @Override public void onPause() {
    super.onPause();
    MobclickAgent.onPageEnd(getPageName());
  }
}
