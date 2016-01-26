package com.ikaowo.join.im.helper;

import com.alibaba.mobileim.aop.Pointcut;
import com.alibaba.mobileim.aop.custom.YWSDKGlobalConfig;

/**
 * Created by leiweibo on 1/26/16.
 */
public class ChattingGlobalConfigCustom extends YWSDKGlobalConfig {

  public ChattingGlobalConfigCustom(Pointcut pointcut) {
    super(pointcut);
  }

  public boolean enableMsgReadStatus() {
    return true;
  }
}
