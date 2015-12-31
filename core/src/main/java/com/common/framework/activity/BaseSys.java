package com.common.framework.activity;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.framework.core.JApplication;
import com.common.framework.core.R;
import com.common.framework.interceptor.JInterceptor;

/**
 * Created by weibo on 15-12-8.
 */
public abstract class BaseSys {
  protected Context context;
  //某些时候，tab对应的fragment被销毁，但是basesys仍然持有对应的fragment的实例，
  //这种情况下，需要把fragment重新返回一个新的实例。一般情况下不会使用
  //这次采用云旺IM工具的时候，因为它所提供的fragment，我们没有办法去给它做刷新
  //因此，在用户登录或者退出的时候，需要把原先的fragment从fragmentmanager中移除，
  //然后再次生成一个加入到其中。
  protected boolean reseted;
  protected Fragment fragment;

  private ViewGroup tabContainer;
  private TabListener listener;
  private View tab;
  private JInterceptor.Stub interceptor;

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

  protected abstract Fragment createFragment();

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

  protected void performClick() {
    if (listener != null) {
      listener.onClicked(this);
    }
  }

  protected void onClicked(boolean b) {
    tab.findViewById(R.id.tab_icon).setSelected(b);
    tab.findViewById(R.id.tab_title).setSelected(b);
  }

  public void setTabTitleTxtColor(int color) {
    ((TextView) tab.findViewById(R.id.tab_title)).setTextColor(context.getResources().getColorStateList(color));
  }

  public abstract String getTag();

  public Fragment getFragment() {
    return fragment;
  }

  public int getMenu() {
    return 0;
  }

  public void setReseted(boolean reseted) {
    this.reseted = reseted;
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


  public interface TabListener {
    void onClicked(BaseSys tab);
  }

}
