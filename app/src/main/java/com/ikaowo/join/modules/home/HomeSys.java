package com.ikaowo.join.modules.home;

import android.content.Context;
import android.view.ViewGroup;

import com.common.framework.activity.BaseSys;
import com.common.framework.core.JFragment;
import com.ikaowo.join.R;

/**
 * Created by weibo on 15-12-8.
 */
public class HomeSys extends BaseSys {

    public HomeSys(Context context, ViewGroup tabContainer, TabListener listener) {
        super(context, tabContainer, listener);
    }

    @Override
    protected JFragment createFragment() {
        return new HomeFragment();
    }

    @Override
    protected int getTabIcon() {
        return R.drawable.home_tab_icon;
    }

    @Override
    protected String getTabTitle() {
        return "首页";
    }

    @Override
    public String getTag() {
        return "HomeSys";
    }

    @Override
    public int getMenu() {
        return R.menu.menu_home;
    }
}
