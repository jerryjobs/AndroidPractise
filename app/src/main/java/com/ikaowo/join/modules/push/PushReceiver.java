package com.ikaowo.join.modules.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.igexin.sdk.PushConsts;
import com.ikaowo.join.modules.push.factory.PushReceiverFactory;
import com.ikaowo.join.modules.push.vendor.PushVendor;

public class PushReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        PushVendor pushVendor = null;
        switch (bundle.getInt(PushConsts.CMD_ACTION)) {
            case PushConsts.GET_CLIENTID:
                pushVendor = new PushReceiverFactory().createGetuiClientIdPush();
                pushVendor.onPushReceived(context, bundle);
                break;

            case PushConsts.GET_MSG_DATA:
                pushVendor = new PushReceiverFactory().createGetuiDataPush();
                pushVendor.onPushReceived(context, bundle);
                break;

            default:
                break;
        }
    }
}
