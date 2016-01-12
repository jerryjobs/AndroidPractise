package com.ikaowo.join.modules.push.factory;

import com.ikaowo.join.modules.push.vendor.GetuiClientPushVender;
import com.ikaowo.join.modules.push.vendor.GetuiDataPushVender;
import com.ikaowo.join.modules.push.vendor.PushVendor;

/**
 * Created by weibo on 16-1-12.
 */
public class PushReceiverFactory {
  public PushVendor createGetuiClientIdPush() {
    return new GetuiClientPushVender();
  }
  public PushVendor createGetuiDataPush() {
    return new GetuiDataPushVender();
  }
}
