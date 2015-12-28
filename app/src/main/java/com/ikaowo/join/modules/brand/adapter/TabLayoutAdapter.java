package com.ikaowo.join.modules.brand.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.common.framework.core.JFragment;
import com.ikaowo.join.modules.brand.fragment.JoinedPromptionListFragment;
import com.ikaowo.join.modules.brand.fragment.PostedPromptionListFragment;
import com.ikaowo.join.modules.brand.fragment.PromptionMemberListFragment;
import com.ikaowo.join.util.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weibo on 15-12-28.
 */
public class TabLayoutAdapter extends FragmentPagerAdapter {

  private String tabTitles[];
  private final int COUNT = 3;
  private int brandId;
  private List<JFragment> fragments = new ArrayList<>();
  private JoinedPromptionListFragment joinedPromptionListFragment;
  private PostedPromptionListFragment postedPromptionListFragment;
  private PromptionMemberListFragment promptionMemberListFragment;


  public TabLayoutAdapter(FragmentManager fm, int brandId, String tabTitles[]) {
    super(fm);
    this.brandId = brandId;
    this.tabTitles = tabTitles;
    initFragments();
  }

  private void initFragments() {
    Bundle companyIdBundle = new Bundle();
    companyIdBundle.putInt(Constant.BRAND_ID, brandId);
    joinedPromptionListFragment = new JoinedPromptionListFragment();
    joinedPromptionListFragment.setArguments(companyIdBundle);
    postedPromptionListFragment = new PostedPromptionListFragment();
    postedPromptionListFragment.setArguments(companyIdBundle);
    promptionMemberListFragment = new PromptionMemberListFragment();
    promptionMemberListFragment.setArguments(companyIdBundle);

    fragments.add(joinedPromptionListFragment);
    fragments.add(postedPromptionListFragment);
    fragments.add(promptionMemberListFragment);
  }

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
    return COUNT;
  }
}
