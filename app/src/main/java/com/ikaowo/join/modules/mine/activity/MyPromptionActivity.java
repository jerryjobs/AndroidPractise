package com.ikaowo.join.modules.mine.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.common.framework.core.JApplication;
import com.ikaowo.join.R;
import com.ikaowo.join.base.BaseFragmentActivity;
import com.ikaowo.join.base.EventBusListener;
import com.ikaowo.join.common.service.UserService;
import com.ikaowo.join.eventbus.GetListCountCallback;
import com.ikaowo.join.modules.mine.adapter.MyPromptionTabLayoutAdapter;

/**
 * Created by leiweibo on 12/29/15.
 */
public class MyPromptionActivity extends BaseFragmentActivity implements EventBusListener {

  @Bind(R.id.tabs) TabLayout tabLayout;
  @Bind(R.id.viewpager) ViewPager viewPager;

  private String[] titlesArray;
  private UserService userService;
  private int brandId;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_my_promption);
    ButterKnife.bind(this);

    toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    toolbar.setTitle(R.string.title_activity_mine_promption);

    ActionBar ab = getSupportActionBar();
    ab.setDisplayHomeAsUpEnabled(true);

    userService = JApplication.getJContext().getServiceByInterface(UserService.class);
    brandId = userService.getUserCompanyId();

    setupView();
  }

  private void setupView() {

    titlesArray = new String[2];
    titlesArray[0] = getString(R.string.tab_title_mine_posted_promption, 0);
    titlesArray[1] = getString(R.string.tab_title_mine_join_promption, 0);

    viewPager.setOffscreenPageLimit(2);
    viewPager.setAdapter(
        new MyPromptionTabLayoutAdapter(getSupportFragmentManager(), titlesArray, brandId));
    tabLayout.setupWithViewPager(viewPager);
  }

  public void onEvent(GetListCountCallback callback) {
    GetListCountCallback.MapObj map = callback.getCountMap();
    String title = null;
    if (map != null) {
      TabLayout.Tab tab = tabLayout.getTabAt(map.index);
      if (tab != null) {
        switch (map.index) {
          case 0:
            title = getString(R.string.tab_title_mine_join_promption, map.count);
            break;

          case 1:
            title = getString(R.string.tab_title_mine_posted_promption, map.count);
            break;
        }
        tabLayout.getTabAt(map.index).setText(title);
      }
    }
  }

  @Override protected String getTag() {
    return "MyPromptionActivity";
  }
}
