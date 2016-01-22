package com.ikaowo.join;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.conversation.IYWConversationService;
import com.alibaba.mobileim.conversation.IYWConversationUnreadChangeListener;
import com.common.framework.activity.BaseSys;
import com.common.framework.activity.TabActivity;
import com.common.framework.core.JApplication;
import com.common.framework.core.JDialogHelper;
import com.common.framework.network.NetworkManager;
import com.common.framework.umeng.UmengService;
import com.component.photo.PhotoUtil;
import com.ikaowo.join.common.service.NotificationService;
import com.ikaowo.join.common.service.PromptionService;
import com.ikaowo.join.common.service.UserService;
import com.ikaowo.join.common.widget.NotificationImageView;
import com.ikaowo.join.eventbus.CheckLatestStateCallback;
import com.ikaowo.join.eventbus.ClickTabCallback;
import com.ikaowo.join.eventbus.SigninCallback;
import com.ikaowo.join.eventbus.SignoutCallback;
import com.ikaowo.join.eventbus.WxKickedOffCallback;
import com.ikaowo.join.im.helper.LoginHelper;
import com.ikaowo.join.im.helper.WxImHelper;
import com.ikaowo.join.model.EnumData;
import com.ikaowo.join.model.UnReadMsg;
import com.ikaowo.join.model.UserLatestState;
import com.ikaowo.join.model.UserLoginData;
import com.ikaowo.join.model.base.BaseListResponse;
import com.ikaowo.join.model.response.EnumDataResponse;
import com.ikaowo.join.modules.brand.BrandSys;
import com.ikaowo.join.modules.message.MessageSys;
import com.ikaowo.join.modules.mine.MineSys;
import com.ikaowo.join.modules.promption.PromptionSys;
import com.ikaowo.join.modules.push.GetuiService;
import com.ikaowo.join.modules.push.factory.PushProcesserFactory;
import com.ikaowo.join.modules.push.model.Push;
import com.ikaowo.join.modules.push.processer.PushDataProcesser;
import com.ikaowo.join.network.CommonInterface;
import com.ikaowo.join.network.KwMarketNetworkCallback;
import com.ikaowo.join.network.NotificationInterface;
import com.ikaowo.join.util.Constant;
import com.ikaowo.join.util.MD5Util;
import com.ikaowo.join.util.SharedPreferenceHelper;
import de.greenrobot.event.EventBus;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import retrofit.Call;

public class MainTabActivity extends TabActivity {

  private UserService userService;
  private UmengService umengService = new UmengService();
  private GetuiService getuiService = new GetuiService();
  private PromptionService promptionService;
  private NotificationService notificationService;

  private IYWConversationService conversationService;
  private YWIMKit imKit;
  private IYWConversationUnreadChangeListener mConversationUnreadChangeListener;
  private Handler mHandler = new Handler(Looper.getMainLooper());
  private String MD5_KEY = "ddl";

  private Map<String, Integer> notificationCnt = new HashMap<>();

  @Override public void onCreate(Bundle savedInstanceState) {
    notificatonStyle = NotificatonStyle.Badge;
    super.onCreate(savedInstanceState);
    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    userService = JApplication.getJContext().getServiceByInterface(UserService.class);
    promptionService = JApplication.getJContext().getServiceByInterface(PromptionService.class);
    notificationService =
        JApplication.getJContext().getServiceByInterface(NotificationService.class);
    getuiService.initGetuiService(this, userService.getUserId());
    if (userService.isLogined()) {
      if (userService.isAuthed()) {
        initWxImKit();
      }
    }

    toolbar.setTitle(getResources().getString(R.string.app_name));
    //        getNotificationCount();
    EventBus.getDefault().register(this);
    getEnumData();

    umengService.init(this);
    umengService.checkUpdate(this, false);

  }

  private void getEnumData() {
    CommonInterface commonInterface =
        JApplication.getNetworkManager().getServiceByClass(CommonInterface.class);
    Call<EnumDataResponse> call = commonInterface.getSystemStateEnum();
    JApplication.getNetworkManager()
        .async(call, new KwMarketNetworkCallback<EnumDataResponse>(this) {
          @Override public void onSuccess(EnumDataResponse response) {
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

      @Override public void onUnreadChange() {
        mHandler.post(new Runnable() {
          @Override public void run() {
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

  @Override protected String getTag() {
    return "MainTabActivity";
  }

  @Override protected List<BaseSys> getTabPages() {
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

  @Override public boolean onCreateOptionsMenu(final Menu menu) {
    super.onCreateOptionsMenu(menu);
    View actionView;

    try {
      if ((actionView = (menu.getItem(0).getActionView())) != null) {
        actionView.setOnClickListener(new View.OnClickListener() {
          @Override public void onClick(View v) {
            onOptionsItemSelected(menu.getItem(0));
          }
        });
        NotificationImageView notificationImageView
            = (NotificationImageView) actionView.findViewById(R.id.notification_img);
        if (notificationCnt.get("SYSTEM") > 0) {
          notificationImageView.showNotification();
        } else {
          notificationImageView.hideNotification();
        }
      }
    } finally {
      return true;
    }
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    switch (id) {
      case R.id.action_add:

        userService.interceptorCheckUserState(this, R.string.action_post_promption,
          new UserService.AuthedAction() {
            @Override public void doActionAfterAuthed() {
              initWxImKit();
              promptionService.goToAddPromotionActivity(MainTabActivity.this);
            }
        });
        break;

      case R.id.action_search:
        promptionService.searchPromptionActivity(this);
        break;

      case R.id.action_view_notification:
        notificationService.viewNotification(this);
        break;
    }

    return super.onOptionsItemSelected(item);
  }

  @Override public void onClicked(final BaseSys tab) {
    menuResId = tab.getMenu();
    invalidateOptionsMenu();
    super.onClicked(tab);
    getNotificationCount();
  }

  @Override protected void getNotificationCount() {
    //Map<String, Integer> map = new HashMap<>();
    //map.put("homesys", 1);
    //map.put("mesys", 3);
    //for (BaseSys tab : tabbarList) {
    //  Integer count = map.get(tab.getTag().toLowerCase());
    //  if (null == count || count == 0) {
    //    tab.hideNotification();
    //  } else {
    //    tab.showNotification(notificatonStyle, count);
    //  }
    //}

    if (clickedPos != 3) {
      return;
    }

    NetworkManager networkManager = JApplication.getNetworkManager();
    NotificationInterface notificationInterface
        = networkManager.getServiceByClass(NotificationInterface.class);
    Call<BaseListResponse<UnReadMsg>> call = notificationInterface.getUnreadMsg();
    networkManager.async(call, new KwMarketNetworkCallback<BaseListResponse<UnReadMsg>>(this) {
      @Override public void onSuccess(BaseListResponse<UnReadMsg> response) {
        if (response != null && response.data != null && response.data.size() > 0){
          List<UnReadMsg> unReadMsgList = response.data;
          for (UnReadMsg msg : unReadMsgList) {
            notificationCnt.put(msg.type, msg.total);
            invalidateOptionsMenu();
          }
        }
      }
    });
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
      getuiService.initGetuiService(this, userService.getUserId());
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
      if (conversationService != null) {
        conversationService.removeTotalUnreadChangeListener(mConversationUnreadChangeListener);
      }
      for (BaseSys tab : tabbarList) {
        tab.hideNotification();
      }
      updateConversationList();
    }
  }

  public void onEvent(CheckLatestStateCallback callback) {
    UserLatestState state = null;
    if ((state = callback.getLatestState()) != null) {
      userService.updateLocalUserInfo(callback.getLatestState());
      if (Constant.AUTH_STATE_PASSED.equalsIgnoreCase(state.sta)) {
        initWxImKit();
      }
    }
  }

  public void onEvent(WxKickedOffCallback callback) {
    if (callback.kickedOff()) {
      dialogHelper.showConfirmDialog(this, "你的账号在别的设备登录，请退出重新登录",
          new JDialogHelper.DoAfterClickCallback() {
            @Override public void doAction() {
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

          Log.e(getTag(),
              "the fragmetnr rmoved?" + (fragmentManager.findFragmentByTag(imTab.getTag())
                  == null));
        }
      }
    } catch (Exception e) {
      Log.e(getTag(), "移除聊天列表fragment失败");
      e.printStackTrace();
    }
  }

  @Override protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    setIntent(intent);
  }

  @Override protected void onPause() {
    super.onPause();
    if (conversationService != null) {
      conversationService.removeTotalUnreadChangeListener(mConversationUnreadChangeListener);
    }
  }

  @Override protected void onResume() {
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
    getNotificationCount();
    handlePushMsg();
  }

  private void handlePushMsg() {
    Bundle bundle = getIntent().getExtras();
    if (bundle == null) {
      return;
    }
    Object objData = getIntent().getExtras().get(Constant.PUSH_INTENT_EXTRA);
    try {
      if (objData != null) {
        Push push = (Push) objData;
        PushDataProcesser pushDataProcesser = new PushProcesserFactory().getDataProcesser(push.type);
        pushDataProcesser.action(this, push.id, push.targetId, push.targetUrl);
        getIntent().removeExtra("data");
      }
    }catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override protected void onDestroy() {
    EventBus.getDefault().unregister(this);
    //用户退出的时候，删除这次操作产生的所有的临时文件
    try {
      PhotoUtil.dropCropImageFolder(this);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
    super.onDestroy();
  }
}
