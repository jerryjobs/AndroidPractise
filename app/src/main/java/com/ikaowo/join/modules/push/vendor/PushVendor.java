package com.ikaowo.join.modules.push.vendor;

import android.content.Context;
import android.os.Bundle;

/**
 * Created by weibo on 16-1-12.
 */
public interface PushVendor {
    void onPushReceived(Context context, Bundle bundle);
}
