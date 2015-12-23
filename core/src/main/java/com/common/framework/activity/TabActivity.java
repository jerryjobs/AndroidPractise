package com.common.framework.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;

import com.common.framework.core.JFragmentActivity;
import com.common.framework.core.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weibo on 15-12-8.
 */
public abstract class TabActivity extends JFragmentActivity implements BaseSys.TabListener {

    public enum NotificatonStyle {
        Normal,
        Badge
    }

    protected List<BaseSys> tabbarList = new ArrayList<>();
    protected LinearLayout tabContainerLayout;
    protected NotificatonStyle notificatonStyle;

    private int clickedPos;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        initTabAndFragment(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        initView();
        setupTab();
    }

    /*
     * 如果 activity被回收，需要把里面的fragment引用也要移除，不然fragment应用一直存在，
     * 但不存活在当前activity
     */
    private void initTabAndFragment(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            savedInstanceState.putParcelable("android:support:fragments", null);
            clickedPos = savedInstanceState.getInt("clickedPos");
        }
    }

    private void initView() {
        tabContainerLayout = (LinearLayout) findViewById(R.id.tab_container);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

    }

    private void setupTab() {
        tabbarList.clear();
        tabbarList.addAll(getTabPages());
        //　select the first tab as default.
        onClicked(tabbarList.get(clickedPos));
    }

    protected abstract List<BaseSys> getTabPages();

    @Override
    public void onClicked(final BaseSys tab) {
        clickedPos = tabbarList.indexOf(tab);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                changeTabBar(tab);
            }
        });

        changeTabPage(tab);
        changeTitle(tab.getActionBarTitle());
    }

    private void changeTabBar(BaseSys tabbar) {
        for (BaseSys tmpTabbar : tabbarList) {
            tmpTabbar.onClicked(tabbar == tmpTabbar);
        }
    }

    private void changeTabPage(BaseSys tabbar) {
        if (tabbar.createFragment() == null) {
            return;
        }
        try {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            for (BaseSys t : tabbarList) {
                if (fragmentManager.findFragmentByTag(t.getTag()) != null) {
                    transaction.hide(t.getFragment());
                }
            }
            if (fragmentManager.findFragmentByTag(tabbar.getTag()) == null) {
                transaction.add(R.id.tab_page_content, tabbar.getFragment(), tabbar.getTag());
            } else {
                Fragment fragment = fragmentManager.findFragmentByTag(tabbar.getTag());
                transaction.show(fragment);
            }
            transaction.commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void changeTitle(String title) {
        toolbar.setTitle(title);
    }

    protected abstract void getNotificationCount();

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected String getTag() {
        return "MainActivity";
    }
}
