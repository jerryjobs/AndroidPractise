package com.ikaowo.join.modules.mine.adapter;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.ikaowo.join.common.adapter.TabLayoutAdapter;
import com.ikaowo.join.modules.brand.fragment.JoinedPromptionListFragment;
import com.ikaowo.join.modules.brand.fragment.PostedPromptionListFragment;
import com.ikaowo.join.util.Constant;

/**
 * Created by leiweibo on 12/29/15.
 */
public class MyPromptionTabLayoutAdapter extends TabLayoutAdapter {

  public MyPromptionTabLayoutAdapter(FragmentManager fm, String[] tabTitles, int brandId) {
    super(fm, tabTitles, brandId);
  }

  @Override
  protected void initFragments(int brandId) {
    Bundle companyIdBundle = new Bundle();
    companyIdBundle.putInt(Constant.BRAND_ID, brandId);
    JoinedPromptionListFragment joinedPromptionListFragment = new JoinedPromptionListFragment();
    joinedPromptionListFragment.setArguments(companyIdBundle);
    PostedPromptionListFragment postedPromptionListFragment = new PostedPromptionListFragment();
    postedPromptionListFragment.setArguments(companyIdBundle);

    fragments.add(joinedPromptionListFragment);
    fragments.add(postedPromptionListFragment);
  }
}
