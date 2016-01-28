package com.ikaowo.join.modules.mine.adapter;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import com.ikaowo.join.common.adapter.TabLayoutAdapter;
import com.ikaowo.join.modules.mine.fragment.UserJoinedPromptionListFragment;
import com.ikaowo.join.modules.mine.fragment.UserPostedPromptionListFragment;
import com.ikaowo.join.util.Constant;

/**
 * Created by leiweibo on 12/29/15.
 */
public class MyPromptionTabLayoutAdapter extends TabLayoutAdapter {

  public MyPromptionTabLayoutAdapter(FragmentManager fm, String[] tabTitles, int brandId) {
    super(fm, tabTitles, brandId);
  }

  @Override protected void initFragments(int brandId) {
    Bundle companyIdBundle = new Bundle();
    companyIdBundle.putInt(Constant.BRAND_ID, brandId);
    companyIdBundle.putBoolean(Constant.SHOW_STATE, true);
    UserJoinedPromptionListFragment userJoinedPromptionListFragment =
        new UserJoinedPromptionListFragment();
    userJoinedPromptionListFragment.setArguments(companyIdBundle);
    UserPostedPromptionListFragment userPostedPromptionListFragment =
        new UserPostedPromptionListFragment();
    userPostedPromptionListFragment.setArguments(companyIdBundle);

    fragments.add(userPostedPromptionListFragment);
    fragments.add(userJoinedPromptionListFragment);
  }
}
