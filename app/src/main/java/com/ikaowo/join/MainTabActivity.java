package com.ikaowo.join;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MenuItem;

import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.conversation.IYWConversationService;
import com.alibaba.mobileim.conversation.IYWConversationUnreadChangeListener;
import com.common.framework.activity.BaseSys;
import com.common.framework.activity.TabActivity;
import com.common.framework.core.JApplication;
import com.common.framework.core.JDialogHelper;
import com.ikaowo.join.common.service.PromptionService;
import com.ikaowo.join.common.service.UserService;
import com.ikaowo.join.eventbus.CheckLatestStateCallback;
import com.ikaowo.join.eventbus.ClickTabCallback;
import com.ikaowo.join.eventbus.SigninCallback;
import com.ikaowo.join.eventbus.SignoutCallback;
import com.ikaowo.join.eventbus.WxKickedOffCallback;
import com.ikaowo.join.im.helper.LoginHelper;
import com.ikaowo.join.im.helper.WxImHelper;
import com.ikaowo.join.model.EnumData;
import com.ikaowo.join.model.UserLoginData;
import com.ikaowo.join.model.response.EnumDataResponse;
import com.ikaowo.join.modules.brand.BrandSys;
import com.ikaowo.join.modules.message.MessageSys;
import com.ikaowo.join.modules.mine.MineSys;
import com.ikaowo.join.modules.promption.PromptionSys;
import com.ikaowo.join.network.CommonInterface;
import com.ikaowo.join.network.KwMarketNetworkCallback;
import com.ikaowo.join.util.MD5Util;
import com.ikaowo.join.util.SharedPreferenceHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;
import retrofit.Call;

public class MainTabActivity extends TabActivity {

  private UserService userService;
  private PromptionService promptionService;

  private IYWConversationService conversationService;
  private YWIMKit imKit;
  private IYWConversationUnreadChangeListener mConversationUnreadChangeListener;
  private Handler mHandler = new Handler(Looper.getMainLooper());
  private String MD5_KEY = "ddl";

  @Override
  public void onCreate(Bundle savedInstanceState) {
    notificatonStyle = NotificatonStyle.Badge;
    super.onCreate(savedInstanceState);
    userService = JApplication.getJContext().getServiceByInterface(UserService.class);
    promptionService = JApplication.getJContext().getServiceByInterface(PromptionService.class);

    if (userService.isAuthed()) {
      initWxImKit();
    }

    toolbar.setTitle(getResources().getString(R.string.app_name));
//        getNotificationCount();
    EventBus.getDefault().register(this);
    getEnumData();

  }

  private void getEnumData() {
    CommonInterface commonInterface = JApplication.getNetworkManager().getServiceByClass(CommonInterface.class);
    Call<EnumDataResponse> call = commonInterface.getSystemStateEnum();
    JApplication.getNetworkManager().async(call, new KwMarketNetworkCallback<EnumDataResponse>(this) {
      @Override
      public void onSuccess(EnumDataResponse response) {
        List<EnumData> enumDataList = null;
        if (response == null || (enumDataList = response.data) == null) {
          return;
        }

        Map<String, String> enumMap = new HashMap<String, String>();
        for (EnumData data : enumDataList) {
          enumMap.put(data.type + data.key, data.value);
        }

        SharedPreferenceHelper sharedPreferenceHelper = SharedPreferenceHelper.getInstance();
        sharedPreferenceHelper.saveEnumValue(MainTabActivity.this, enumMap);
      }
    });
  }

  private void initWxImKit() {
    imKit = LoginHelper.getInstance().getIMKit();
    if (imKit != null) {
      UserLoginData user = userService.getUser();
      WxImHelper.getInstance().initWxService(this, MD5Util.md5(user.phone + MD5_KEY), user.wxId);
      conversationService = imKit.getConversationService();
      initConversationServiceAndListener();
    }
  }

  private void initConversationServiceAndListener() {
    mConversationUnreadChangeListener = new IYWConversationUnreadChangeListener() {

      @Override
      public void onUnreadChange() {
        mHandler.post(new Runnable() {
          @Override
          public void run() {
            LoginHelper loginHelper = LoginHelper.getInstance();
            final YWIMKit imKit = loginHelper.getIMKit();
            conversationService = imKit.getConversationService();
            int unReadCount = conversationService.getAllUnreadCount();
            if (unReadCount > 0) {
              tabbarList.get(2).showNotification(notificatonStyle, unReadCount);
            } else {
              tabbarList.get(2).hideNotification();
            }
          }
        });
      }
    };
  }

  @Override
  protected String getTag() {
    return "MainTabActivity";
  }

  @Override
  protected List<BaseSys> getTabPages() {
    List<BaseSys> tabList = new ArrayList<>();
    PromptionSys homeTab = new PromptionSys(this, tabContainerLayout, this);
    tabList.add(homeTab);
    homeTab.setTabTitleTxtColor(R.color.tab_title);

    BrandSys companyTab = new BrandSys(this, tabContainerLayout, this);
    companyTab.setTabTitleTxtColor(R.color.tab_title);
    tabList.add(companyTab);

    MessageSys msgTab = new MessageSys(this, tabContainerLayout, this);
    msgTab.setTabTitleTxtColor(R.color.tab_title);
    tabList.add(msgTab);

    MineSys meTab = new MineSys(this, tabContainerLayout, this);
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


    switch (id) {
      case R.id.action_add:

        userService.interceptorCheckUserState(this, new UserService.AuthedAction() {
          @Override
          public void doActionAfterAuthed() {
            initWxImKit();
            promptionService.goToAddPromotionActivity(MainTabActivity.this);
          }
        });
        break;

      case R.id.action_search:
        promptionService.searchPromptionActivity(this);
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

  public void onEvent(ClickTabCallback callback) {
    int pos = callback.getClickedSys();
    BaseSys baseSys = tabbarList.get(pos);
    if (baseSys != null) {
      onClicked(baseSys);
    }
  }

  public void onEvent(SigninCallback callback) {
    if (callback.singined()) {
      if (userService.isAuthed()) {
        initWxImKit();
        updateConversationList();
      }

      if (callback.changeTab()) {
        onClicked(tabbarList.get(3));
      }
    }
  }

  public void onEvent(SignoutCallback callback) {
    if (callback.signout()) {
      conversationService.removeTotalUnreadChangeListener(mConversationUnreadChangeListener);
      for (BaseSys tab: tabbarList) {
        tab.hideNotification();
      }
      updateConversationList();
    }
  }

  public void onEvent(CheckLatestStateCallback callback) {
    if (callback.authed()) {
      userService.updateLocalUserInfo(true);
      initWxImKit();
    }
  }

  public void onEvent(WxKickedOffCallback callback) {
    if (callback.kickedOff()) {
      dialogHelper.showConfirmDialog(this, "你的账号在别的设备登录，请退出重新登录", new JDialogHelper.DoAfterClickCallback() {
        @Override
        public void doAction() {
          userService.logout(MainTabActivity.this);
        }
      });
    }
  }

  private void updateConversationList() {
    //退出或者重新登录之后，将聊天列表的fragment从fragmentmanager里面移除
    try {
      BaseSys imTab = tabbarList.get(2);
      if (imTab != null) {
        imTab.setReseted(true);
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(imTab.getTag());
        if (fragment != null) {

          FragmentTransaction transaction = fragmentManager.beginTransaction();
          transaction.remove(fragment);
          transaction.commitAllowingStateLoss();
          Log.e(getTag(), "remove the fragment finished");

          Log.e(getTag(), "the fragmetnr rmoved?" + (fragmentManager.findFragmentByTag(imTab.getTag()) == null));
        }
      }
    } catch (Exception e) {
      Log.e(getTag(), "移除聊天列表fragment失败");
      e.printStackTrace();
    }
  }

  @Override
  protected void onPause() {
    super.onPause();
    if (conversationService != null) {
      conversationService.removeTotalUnreadChangeListener(mConversationUnreadChangeListener);
    }
  }

  @Override
  protected void onResume() {
    super.onResume();

    LoginHelper loginHelper = LoginHelper.getInstance();
    final YWIMKit imKit = loginHelper.getIMKit();
    if (imKit != null && mConversationUnreadChangeListener != null) {
      conversationService = imKit.getConversationService();
      //resume时需要检查全局未读消息数并做处理，因为离开此界面时删除了全局消息监听器
      mConversationUnreadChangeListener.onUnreadChange();

      //在Tab栏增加会话未读消息变化的全局监听器
      if (conversationService != null) {
        conversationService.addTotalUnreadChangeListener(mConversationUnreadChangeListener);
      }
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    EventBus.getDefault().unregister(this);
  }
}
