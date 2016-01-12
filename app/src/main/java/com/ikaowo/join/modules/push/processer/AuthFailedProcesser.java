package com.ikaowo.join.modules.push.processer;

import android.content.Context;
import com.ikaowo.join.common.service.UserService;

/**
 * Created by weibo on 16-1-12.
 */
public class AuthFailedProcesser extends PushDataProcesser {

  @Override public void action(Context context, int targetId) {
    UserService userService = jContext.getServiceByInterface(UserService.class);
    userService.reSubmitInfo(context);
  }
}
