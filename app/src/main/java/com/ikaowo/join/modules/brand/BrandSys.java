package com.ikaowo.join.modules.brand;

import android.content.Context;
import android.view.ViewGroup;

import com.common.framework.activity.BaseSys;
import com.common.framework.core.JFragment;
import com.ikaowo.join.R;
import com.ikaowo.join.modules.user.fragment.BrandListFragment;

/**
 * Created by weibo on 15-12-8.
 */
public class BrandSys extends BaseSys {

    public BrandSys(Context context, ViewGroup tabContainer, TabListener listener) {
        super(context, tabContainer, listener);
    }

    @Override
    protected JFragment createFragment() {
        return new BrandListFragment();
    }

    @Override
    protected int getTabIcon() {
        return R.drawable.company_tab_icon;
    }

    @Override
    protected String getTabTitle() {
        return context.getString(R.string.title_brand);
    }

    @Override
    protected String getActionBarTitle() {
        return context.getResources().getString(R.string.title_brand);
    }

    @Override
    public String getTag() {
        return "BrandSys";
    }
}
