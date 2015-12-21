package com.ikaowo.join;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.common.framework.core.JFragment;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by weibo on 15-12-8.
 */
public abstract class BaseFragment extends JFragment {

  protected void showInputMethod(final View view) {
    view.postDelayed(new Runnable() {
      @Override
      public void run() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
          imm.showSoftInput(view, 0);
        }
      }
    }, 200);

  }

  public abstract String getPageName();

  @Override
  public void onResume() {
    super.onResume();
    MobclickAgent.onPageStart(getPageName());
  }

  @Override
  public void onPause() {
    super.onPause();
    MobclickAgent.onPageEnd(getPageName());
  }
}
