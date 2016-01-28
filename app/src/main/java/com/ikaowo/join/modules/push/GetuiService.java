package com.ikaowo.join.modules.push;

import android.content.Context;
import com.igexin.sdk.PushManager;

/**
 * Created by weibo on 16-1-12.
 */
public class GetuiService {
  public void initGetuiService(Context context, int uId) {
    PushManager.getInstance().initialize(context);
    if (uId > 0) {
      PushManager.getInstance().bindAlias(context, String.valueOf(uId));
    }
  }

  public void unBindGetuiService(Context context, int uId) {
    PushManager.getInstance().unBindAlias(context, String.valueOf(uId), false);
  }
}
