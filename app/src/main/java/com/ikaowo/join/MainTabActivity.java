package com.ikaowo.join;

import android.os.Bundle;
import android.view.MenuItem;

import com.common.framework.activity.BaseSys;
import com.common.framework.activity.TabActivity;
import com.common.framework.core.JApplication;
import com.ikaowo.join.common.service.UserService;
import com.ikaowo.join.modules.brand.BrandSys;
import com.ikaowo.join.modules.home.HomeSys;
import com.ikaowo.join.modules.message.MessageSys;
import com.ikaowo.join.modules.mine.MineSys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainTabActivity extends TabActivity {

  private UserService userService;

  @Override
  public void onCreate(Bundle savedInstanceState) {
//        notificatonStyle = NotificatonStyle.Badge;
    super.onCreate(savedInstanceState);
    userService = JApplication.getJContext().getServiceByInterface(UserService.class);
    toolbar.setTitle(getResources().getString(R.string.app_name));
//        getNotificationCount();

  }

  @Override
  protected String getTag() {
    return "MainTabActivity";
  }

  @Override
  protected List<BaseSys> getTabPages() {
    List<BaseSys> tabList = new ArrayList<>();
    BaseSys homeTab = new HomeSys(this, tabContainerLayout, this);
    tabList.add(homeTab);
    homeTab.setTabTitleTxtColor(R.color.tab_title);

    BaseSys companyTab = new BrandSys(this, tabContainerLayout, this);
    companyTab.setTabTitleTxtColor(R.color.tab_title);
    tabList.add(companyTab);

    BaseSys msgTab = new MessageSys(this, tabContainerLayout, this);
    msgTab.setTabTitleTxtColor(R.color.tab_title);
    tabList.add(msgTab);

    BaseSys meTab = new MineSys(this, tabContainerLayout, this);
    meTab.setTabTitleTxtColor(R.color.tab_title);
    tabList.add(meTab);
    return tabList;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();



    switch(id) {
      case R.id.action_add:
        if (userService.isLogined()) {

        } else {
          userService.goToSignin(this);
        }
        break;

      case R.id.action_search:

        break;
    }

    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onClicked(final BaseSys tab) {
    menuResId = tab.getMenu();
    invalidateOptionsMenu();
    super.onClicked(tab);
  }

  @Override
  protected void getNotificationCount() {
    Map<String, Integer> map = new HashMap<>();
    map.put("homesys", 1);
    map.put("mesys", 3);
    for (BaseSys tab : tabbarList) {
      Integer count = map.get(tab.getTag().toLowerCase());
      if (null == count || count == 0) {
        tab.hideNotification();
      } else {
        tab.showNotification(notificatonStyle, count);
      }
    }
  }
}
