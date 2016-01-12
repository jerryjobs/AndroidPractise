package com.ikaowo.join.modules.push.vendor;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ikaowo.join.modules.push.factory.PushProcesserFactory;
import com.ikaowo.join.modules.push.processer.PushDataProcesser;
import com.ikaowo.join.modules.push.model.GetuiReceiveData;

/**
 * Created by weibo on 16-1-12.
 */
public class GetuiDataPushVender implements PushVendor {

  @Override public void onPushReceived(Context context, Bundle bundle) {
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
      processer.process(context, getuiReceiveData);
    }
  }

}
