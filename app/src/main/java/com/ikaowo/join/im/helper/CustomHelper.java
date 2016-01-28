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
    AdviceBinder.bindAdvice(PointCutEnum.CHATTING_FRAGMENT_UI_POINTCUT, ChattingUICustom.class);
    AdviceBinder.bindAdvice(PointCutEnum.YWSDK_GLOBAL_CONFIG_POINTCUT,
        ChattingGlobalConfigCustom.class);
    AdviceBinder.bindAdvice(PointCutEnum.CONVERSATION_FRAGMENT_POINTCUT,
        ConversationListUICustom.class);
  }
}
