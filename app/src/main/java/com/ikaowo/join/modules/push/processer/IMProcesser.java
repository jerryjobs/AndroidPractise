package com.ikaowo.join.modules.push.processer;

import android.content.Context;
import com.common.framework.core.JApplication;
import com.ikaowo.join.common.service.UserService;

/**
 * Created by weibo on 16-1-14.
 */
public class IMProcesser extends PushDataProcesser {
  @Override public void action(Context context, String target) {
    UserService userService = JApplication.getJContext().getServiceByInterface(UserService.class);
    userService.imChat(context, target);
  }
}
