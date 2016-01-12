package com.ikaowo.join.modules.push.processer;

import android.content.Context;
import android.content.Intent;
import com.common.framework.core.JApplication;
import com.common.framework.core.JContext;
import com.common.framework.network.NetworkManager;
import com.common.framework.util.JLog;
import com.ikaowo.join.MainTabActivity;
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
  public void process(final Context context, GetuiReceiveData data) {
    Intent intent = new Intent();
    //Push push = new Push();
    intent.setClass(context, MainTabActivity.class);
    Push push = new Push();
    push.type = data.type;
    push.targetId = data.rsId;

    NetworkManager networkManager = JApplication.getNetworkManager();
    NotificationInterface notificationInterface = networkManager.getServiceByClass(
        NotificationInterface.class);
    Map<String, Integer> map = new HashMap<>();
    map.put("nt_id", data.ntId);
    Call<BaseResponse> call = notificationInterface.markAsRed(map);
    networkManager.async(call, new KwMarketNetworkCallback<BaseResponse>(context) {
      @Override public void onSuccess(BaseResponse response) {
        JLog.e("PushDatProcesser", "state update success");
      }
    });
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
    intent.putExtra(Constant.PUSH_INTENT_EXTRA, push);
    new NotificationHelper().displayNotification(context, data.title, data.content, intent);
  }
  public abstract void action(Context context, int targetId);

}
