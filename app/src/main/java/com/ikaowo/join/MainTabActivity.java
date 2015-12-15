package com.ikaowo.join;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.common.framework.activity.BaseSys;
import com.common.framework.activity.TabActivity;
import com.ikaowo.join.modules.company.CompanySys;
import com.ikaowo.join.modules.home.HomeSys;
import com.ikaowo.join.modules.mine.MineSys;
import com.ikaowo.join.modules.message.MessageSys;
import com.ikaowo.join.modules.user.SigninActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainTabActivity extends TabActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
//        notificatonStyle = NotificatonStyle.Badge;
        super.onCreate(savedInstanceState);
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

        BaseSys companyTab = new CompanySys(this, tabContainerLayout, this);
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

        Intent intent = new Intent(MainTabActivity.this, SigninActivity.class);
        startActivity(intent);

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
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
