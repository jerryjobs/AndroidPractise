package com.ikaowo.join.modules.push.vendor;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.common.framework.core.JApplication;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ikaowo.join.common.service.UserService;
import com.ikaowo.join.model.UserLatestState;
import com.ikaowo.join.modules.push.factory.PushProcesserFactory;
import com.ikaowo.join.modules.push.model.GetuiReceiveData;
import com.ikaowo.join.modules.push.processer.PushDataProcesser;
import com.ikaowo.join.util.Constant;

/**
 * Created by weibo on 16-1-12.
 */
public class GetuiDataPushVender implements PushVendor {

    @Override
    public void onPushReceived(Context context, Bundle bundle) {
        byte[] payload = bundle.getByteArray("payload");

        if (payload == null) {
            return;
        }

        String payloadStr = new String(payload);
        Log.e("Push Receive:", "the received msg:" + payloadStr);
        Gson gson = new GsonBuilder().create();

        final GetuiReceiveData getuiReceiveData = gson.fromJson(payloadStr, GetuiReceiveData.class);
        PushDataProcesser processer = new PushProcesserFactory().getDataProcesser(getuiReceiveData.type);
        if (processer != null) {
            if (getuiReceiveData != null || Constant.PUSH_ACCT_PASSED.equalsIgnoreCase(getuiReceiveData.type)
                    || Constant.PUSH_ACCT_FAILED.equalsIgnoreCase(getuiReceiveData.type)) {
                UserLatestState userLatestState = new UserLatestState();
                userLatestState.sta = Constant.PUSH_ACCT_FAILED.equalsIgnoreCase(getuiReceiveData.type) ? "F" : "T";

                UserService userService = JApplication.getJContext().getServiceByInterface(UserService.class);
                userService.updateLocalUserInfo(userLatestState);
            }

            processer.process(context, getuiReceiveData);
        }
    }

}
