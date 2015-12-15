package com.ikaowo.join.modules.company;

import android.content.Context;
import android.view.ViewGroup;

import com.common.framework.activity.BaseSys;
import com.common.framework.core.JFragment;
import com.ikaowo.join.R;

/**
 * Created by weibo on 15-12-8.
 */
public class CompanySys extends BaseSys {

    public CompanySys(Context context, ViewGroup tabContainer, TabListener listener) {
        super(context, tabContainer, listener);
    }

    @Override
    protected JFragment createFragment() {
        return new CompanyFragment();
    }

    @Override
    protected int getTabIcon() {
        return R.drawable.company_tab_icon;
    }

    @Override
    protected String getTabTitle() {
        return "公司";
    }

    @Override
    protected String getActionBarTitle() {
        return context.getResources().getString(R.string.app_name);
    }

    @Override
    public String getTag() {
        return "CompanySys";
    }

    @Override
    public int getMenu() {
        return R.menu.menu_company;
    }
}
