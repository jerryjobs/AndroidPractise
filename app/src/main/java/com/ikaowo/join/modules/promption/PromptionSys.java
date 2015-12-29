package com.ikaowo.join.modules.promption;

import android.content.Context;
import android.view.ViewGroup;

import com.common.framework.activity.BaseSys;
import com.common.framework.core.JFragment;
import com.ikaowo.join.R;
import com.ikaowo.join.modules.promption.fragment.PromptionListFragment;

/**
 * Created by weibo on 15-12-8.
 */
public class PromptionSys extends BaseSys {

  public PromptionSys(Context context, ViewGroup tabContainer, TabListener listener) {
    super(context, tabContainer, listener);
  }

  @Override
  protected JFragment createFragment() {
    return new PromptionListFragment();
  }

  @Override
  protected int getTabIcon() {
    return R.drawable.home_tab_icon;
  }

  @Override
  protected String getTabTitle() {
    return context.getString(R.string.title_promption);
  }

  @Override
  protected String getActionBarTitle() {
    return context.getResources().getString(R.string.title_promption);
  }

  @Override
  public String getTag() {
    return "PromptionSys";
  }

  @Override
  public int getMenu() {
    return R.menu.menu_home;
  }
}
