package com.ikaowo.join.modules.push;

import android.content.Context;
import com.igexin.sdk.PushManager;

/**
 * Created by weibo on 16-1-12.
 */
public class GetuiService {
  public void initGetuiService(Context context) {
    PushManager.getInstance().initialize(context);
  }

  public void stopGetuiService(Context context) {
    PushManager.getInstance().stopService(context);
  }
}
