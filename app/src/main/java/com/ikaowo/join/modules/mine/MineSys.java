package com.ikaowo.join.modules.mine;

import android.content.Context;
import android.view.ViewGroup;

import com.common.framework.activity.BaseSys;
import com.common.framework.core.JFragment;
import com.ikaowo.join.R;
import com.ikaowo.join.modules.mine.fragment.MineFragment;

/**
 * Created by weibo on 15-12-8.
 */
public class MineSys extends BaseSys {

  public MineSys(Context context, ViewGroup tabContainer, TabListener listener) {
    super(context, tabContainer, listener);
  }

  @Override
  protected JFragment createFragment() {
    return new MineFragment();
  }

  @Override
  protected int getTabIcon() {
    return R.drawable.mine_tab_icon;
  }

  @Override
  protected String getTabTitle() {
    return "我";
  }

  @Override
  protected String getActionBarTitle() {
    return context.getResources().getString(R.string.app_name);
  }

  @Override
  public String getTag() {
    return "MineSys";
  }

}
