package com.ikaowo.join.common.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.common.framework.core.JFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weibo on 15-12-28.
 */
public abstract class TabLayoutAdapter extends FragmentPagerAdapter {

    protected String tabTitles[];

    protected List<JFragment> fragments = new ArrayList<>();

    public TabLayoutAdapter(FragmentManager fm, String tabTitles[], int brandId) {
        super(fm);
        this.tabTitles = tabTitles;
        initFragments(brandId);
    }

    protected abstract void initFragments(int brandId);

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
