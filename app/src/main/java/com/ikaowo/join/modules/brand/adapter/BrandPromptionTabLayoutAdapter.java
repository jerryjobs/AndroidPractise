package com.ikaowo.join.modules.brand.adapter;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import com.ikaowo.join.common.adapter.TabLayoutAdapter;
import com.ikaowo.join.modules.brand.fragment.JoinedPromptionListFragment;
import com.ikaowo.join.modules.brand.fragment.PostedPromptionListFragment;
import com.ikaowo.join.modules.brand.fragment.PromptionMemberListFragment;
import com.ikaowo.join.util.Constant;

/**
 * Created by leiweibo on 12/29/15.
 */
public class BrandPromptionTabLayoutAdapter extends TabLayoutAdapter {
  protected int brandId;

  public BrandPromptionTabLayoutAdapter(FragmentManager fm, String[] tabTitles, int brandId) {
    super(fm, tabTitles, brandId);
    this.brandId = brandId;
  }

  @Override protected void initFragments(int brandId) {
    Bundle companyIdBundle = new Bundle();
    companyIdBundle.putInt(Constant.BRAND_ID, brandId);
    companyIdBundle.putBoolean(Constant.SHOW_STATE, false);
    JoinedPromptionListFragment joinedPromptionListFragment = new JoinedPromptionListFragment();
    joinedPromptionListFragment.setArguments(companyIdBundle);
    PostedPromptionListFragment postedPromptionListFragment = new PostedPromptionListFragment();
    postedPromptionListFragment.setArguments(companyIdBundle);
    PromptionMemberListFragment promptionMemberListFragment = new PromptionMemberListFragment();
    promptionMemberListFragment.setArguments(companyIdBundle);

    fragments.add(joinedPromptionListFragment);
    fragments.add(postedPromptionListFragment);
    fragments.add(promptionMemberListFragment);
  }
}
