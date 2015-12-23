package com.common.framework.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.framework.core.JApplication;
import com.common.framework.core.JFragment;
import com.common.framework.core.R;
import com.common.framework.interceptor.JInterceptor;

/**
 * Created by weibo on 15-12-8.
 */
public abstract class BaseSys {
    protected Context context;

    private ViewGroup tabContainer;
    private TabListener listener;
    private View tab;
    private JInterceptor.Stub interceptor;
    private JFragment fragment;

    public BaseSys(Context context, ViewGroup tabContainer, TabListener listener) {
        this.context = context;
        this.tabContainer = tabContainer;
        this.listener = listener;
        this.tab = createTab();
        this.interceptor = createInterceptor();
        this.fragment = createFragment();

        setupTabBar(tabContainer);
    }
    private View createTab() {
        View tabView = LayoutInflater.from(context).inflate(R.layout.view_tab, null);
        return tabView;
    }

    protected JInterceptor.Stub createInterceptor() {
        return null;
    }

    protected abstract JFragment createFragment();

    private void setupTabBar(ViewGroup tabContainer) {
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        llp.weight = 1;
        tabContainer.addView(tab, llp);

        ((ImageView) tab.findViewById(R.id.tab_icon)).setImageResource(getTabIcon());
        ((TextView) tab.findViewById(R.id.tab_title)).setText(getTabTitle());
        tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (interceptor != null) {
                    interceptor.check();
                } else {
                    performClick();
                }
            }
        });
    }
    protected abstract int getTabIcon();

    protected abstract String getTabTitle();

    protected abstract String getActionBarTitle();

    private void performClick() {
        if (listener != null) {
            listener.onClicked(this);
        }
    }

    protected void onClicked(boolean b) {
        tab.findViewById(R.id.tab_icon).setSelected(b);
        tab.findViewById(R.id.tab_title).setSelected(b);
    }

    public void setTabTitleTxtColor(int color) {
        ((TextView)tab.findViewById(R.id.tab_title)).setTextColor(context.getResources().getColorStateList(color));
    }

    public abstract String getTag();

    public JFragment getFragment() {
        return fragment;
    }

    public interface TabListener {
        void onClicked(BaseSys tab);
    }

    public int getMenu() {
        return 0;
    }

    private void showBadgeNotification(int count) {
        TextView tv = (TextView) tab.findViewById(R.id.unread);
        tv.setText(String.valueOf(count));
        tv.setVisibility(View.VISIBLE);
    }

    public void hideNotification() {
        TextView tv = (TextView) tab.findViewById(R.id.unread);
        tv.setVisibility(View.GONE);
    }

    private void showNormalNotification() {
        TextView tv = (TextView) tab.findViewById(R.id.unread);
        ViewGroup.LayoutParams layoutParams = tv.getLayoutParams();
        layoutParams.width = JApplication.getJContext().dip2px(16);
        layoutParams.height = JApplication.getJContext().dip2px(16);
        tv.setLayoutParams(layoutParams);
        tv.setVisibility(View.VISIBLE);
    }


    public void showNotification(TabActivity.NotificatonStyle notificatonStyle, int count) {
        switch (notificatonStyle) {
            case Normal:
                showNormalNotification();
                break;

            case Badge:
                showBadgeNotification(count);
                break;
        }
    }

}
