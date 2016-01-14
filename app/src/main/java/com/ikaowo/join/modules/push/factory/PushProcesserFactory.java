package com.ikaowo.join.modules.push.factory;

import android.text.TextUtils;
import com.ikaowo.join.modules.push.processer.AuthFailedProcesser;
import com.ikaowo.join.modules.push.processer.AuthSucProcesser;
import com.ikaowo.join.modules.push.processer.IMProcesser;
import com.ikaowo.join.modules.push.processer.JoinNewProcesser;
import com.ikaowo.join.modules.push.processer.JoinedProcesser;
import com.ikaowo.join.modules.push.processer.PromptionFailedProcesser;
import com.ikaowo.join.modules.push.processer.PromptionPassedProcesser;
import com.ikaowo.join.modules.push.processer.PushDataProcesser;
import com.ikaowo.join.modules.push.processer.SystemNotificationProcesser;
import com.ikaowo.join.util.Constant;

/**
 * Created by weibo on 16-1-12.
 */
public class PushProcesserFactory {

  public PushDataProcesser getDataProcesser(String type) {
    PushDataProcesser processer = null;
    if (TextUtils.isEmpty(type)) {
      return null;
    }
    switch (type) {
      case Constant.PUSH_JOIN_NEW:
        processer = new JoinNewProcesser();
        break;

      case Constant.PUSH_JOIN_JOINED:
        processer = new JoinedProcesser();
        break;

      case Constant.PUSH_ACCT_PASSED:
        processer = new AuthSucProcesser();
        break;

      case Constant.PUSH_ACCT_FAILED:
        processer = new AuthFailedProcesser();
        break;

      case Constant.PUSH_PROMPTION_PASSED:
        processer = new PromptionPassedProcesser();
        break;

      case Constant.PUSH_PROMPTION_FAILED:
        processer = new PromptionFailedProcesser();
        break;

      case Constant.PUSH_SYS_1:
      case Constant.PUSH_SYS_2:
        processer = new SystemNotificationProcesser();
        break;

      case Constant.IM_CHAT:
        processer = new IMProcesser();
        break;
    }

    return processer;
  }
}
