package com.ikaowo.join.im.helper;

import com.alibaba.mobileim.aop.AdviceBinder;
import com.alibaba.mobileim.aop.PointCutEnum;

/**
 * Created by weibo on 15-12-30.
 */
public class CustomHelper {
  public static void initCustom() {
    //消息通知栏
    AdviceBinder.bindAdvice(PointCutEnum.NOTIFICATION_POINTCUT, NotificationInitHelper.class);
  }
}