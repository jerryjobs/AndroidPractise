package com.ikaowo.join.modules.push.processer;

import android.content.Context;
import android.content.Intent;
import com.common.framework.core.JApplication;
import com.common.framework.core.JContext;
import com.common.framework.network.NetworkManager;
import com.common.framework.util.JLog;
import com.ikaowo.join.MainTabActivity;
import com.ikaowo.join.common.service.UserService;
import com.ikaowo.join.model.base.BaseResponse;
import com.ikaowo.join.modules.push.common.NotificationHelper;
import com.ikaowo.join.modules.push.model.GetuiReceiveData;
import com.ikaowo.join.modules.push.model.Push;
import com.ikaowo.join.network.KwMarketNetworkCallback;
import com.ikaowo.join.network.NotificationInterface;
import com.ikaowo.join.util.Constant;
import java.util.HashMap;
import java.util.Map;
import retrofit.Call;

/**
 * Created by weibo on 16-1-12.
 */
public abstract class PushDataProcesser {
  protected JContext jContext = JApplication.getJContext();
  protected boolean isRead;

  public void process(final Context context, GetuiReceiveData data) {
    Intent intent = new Intent();
    //Push push = new Push();
    intent.setClass(context, MainTabActivity.class);
    Push push = new Push();
    push.id = data.ntId;
    push.type = data.type;
    push.targetId = data.rsId;
    push.targetUrl = data.targetUrl;

    UserService userService = JApplication.getJContext().getServiceByInterface(UserService.class);
    if (userService.isLogined()) {//如果为已经登录，那么显示正常的推送
      intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
      intent.putExtra(Constant.PUSH_INTENT_EXTRA, push);
      new NotificationHelper().displayNotification(context, data.title, data.content, intent);
    } else {
      if (Constant.PUSH_SYS_1.equalsIgnoreCase(data.type) || Constant.PUSH_SYS_2.equalsIgnoreCase(
          data.type)) {
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(Constant.PUSH_INTENT_EXTRA, push);
        new NotificationHelper().displayNotification(context, data.title, data.content, intent);
      }
    }
  }

  /**
   * 点击系统推送以及系统列表时候调用
   *
   * @param ntId 消息的id， 标记已读时候需要用到
   * @param target 需要跳转的id,目前包含用户id， 活动的id以及 wxid
   * @param targetUrl 系统通知的时候，需要跳转到webview。
   */
  public void action(Context context, int ntId, String target, String targetUrl) {
    UserService userService = JApplication.getJContext().getServiceByInterface(UserService.class);
    if (!isRead && userService.isLogined()) {
      NetworkManager networkManager = JApplication.getNetworkManager();
      NotificationInterface notificationInterface =
          networkManager.getServiceByClass(NotificationInterface.class);
      Map<String, Integer> map = new HashMap<>();
      map.put("nt_id", ntId);
      Call<BaseResponse> call = notificationInterface.markAsRed(map);
      networkManager.async(call, new KwMarketNetworkCallback<BaseResponse>(context) {
        @Override public void onSuccess(BaseResponse response) {
          JLog.e("PushDatProcesser", "state update success");
        }
      });
    }

    openPage(context, target, targetUrl);
  }

  public void action(Context context, int ntId, boolean isRead, String target, String targetUrl) {
    this.isRead = isRead;
    this.action(context, ntId, target, targetUrl);
  }

  public abstract void openPage(Context context, String target, String targetUrl);
}
