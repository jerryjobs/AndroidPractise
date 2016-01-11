package com.ikaowo.join.modules.mine;

import android.content.Context;
import android.view.ViewGroup;

import com.common.framework.activity.BaseSys;
import com.common.framework.core.JApplication;
import com.common.framework.core.JFragment;
import com.common.framework.interceptor.JInterceptor;
import com.ikaowo.join.R;
import com.ikaowo.join.common.service.UserService;
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

  protected JInterceptor.Stub createInterceptor() {
    return new JInterceptor.Stub() {
      public boolean check() {
        UserService userService = JApplication.getJContext().getServiceByInterface(UserService.class);
        if (!userService.isLogined()) {
          userService.goToSignin(context);
        } else {
          performClick();
        }
        return false;
      }
    };
  }

  @Override
  protected int getTabIcon() {
    return R.drawable.mine_tab_icon;
  }

  @Override
  protected String getTabTitle() {
    return context.getString(R.string.title_mine);
  }

  @Override
  protected String getActionBarTitle() {
    return context.getResources().getString(R.string.title_mine);
  }

  @Override
  public int getMenu() {
    return R.menu.menu_me;
  }

  @Override
  public String getTag() {
    return "MineSys";
  }

}
