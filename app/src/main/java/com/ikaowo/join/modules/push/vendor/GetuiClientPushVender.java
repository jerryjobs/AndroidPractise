package com.ikaowo.join.modules.push.vendor;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import com.common.framework.core.JApplication;
import com.common.framework.network.NetworkManager;
import com.ikaowo.join.common.service.UserService;
import com.ikaowo.join.model.PushClientIdRequest;
import com.ikaowo.join.model.base.BaseResponse;
import com.ikaowo.join.network.KwMarketNetworkCallback;
import com.ikaowo.join.network.PushInterface;
import com.ikaowo.join.util.Constant;
import retrofit.Call;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by weibo on 16-1-12.
 */
public class GetuiClientPushVender implements PushVendor {
  NetworkManager networkManager = JApplication.getNetworkManager();
  UserService userService = JApplication.getJContext().getServiceByInterface(UserService.class);

  @Override public void onPushReceived(final Context context, Bundle bundle) {

    if (!userService.isLogined()) { //如果未登录，那么不上报个推ID
      return;
    }

    final String cid = bundle.getString("clientid");
    PushInterface pushInterface = networkManager.getServiceByClass(PushInterface.class);
    PushClientIdRequest request = new PushClientIdRequest();
    request.u_id = userService.getUserId();
    request.getui_id = cid;
    request.sta = Constant.GETUI_STATE_T;
    request.sys = Constant.SYS;
    request.version = JApplication.getJContext().getVersionName();
    Call<BaseResponse> call = pushInterface.updatePushClientId(request);
    networkManager.async(call, new KwMarketNetworkCallback<BaseResponse>(context) {
      @Override public void onSuccess(BaseResponse response) {
        Log.d("getui", "getui upload completed");
      }

      @Override public void onFailed(Response response, Retrofit retrofit) {
        super.onFailed(response, retrofit);
        Log.e("getui", "getui upload failed.");
      }
    });
  }
}
