package com.ikaowo.join.im.helper;

import android.content.Intent;

import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.aop.Pointcut;
import com.alibaba.mobileim.aop.custom.IMNotification;
import com.alibaba.mobileim.conversation.YWConversation;
import com.alibaba.mobileim.conversation.YWMessage;
import com.ikaowo.join.R;

/**
 * Created by weibo on 15-12-30.
 */
public class NotificationInitHelper extends IMNotification {
  private static boolean needQuiet;
  private static boolean needSound = true;
  private static boolean needVibrator = true;

  public NotificationInitHelper(Pointcut pointcut) {
    super(pointcut);
  }

  public static void init() {
    YWIMKit imKit = LoginHelper.getInstance().getIMKit();

    if (imKit != null) {
      needSound = true;
      needVibrator = true;
    }
  }

  public void setNeedQuiet(boolean needQuiet) {
    this.needQuiet = needQuiet;
  }

  public void setNeedSound(boolean needSound) {
    this.needSound = needSound;
  }

  public void setNeedVibrator(boolean needVibrator) {
    this.needVibrator = needVibrator;
  }

  /**
   * 是否开启免打扰模式，若开启免打扰模式则收到新消息时不发送通知栏提醒，只在会话列表页面显示未读数
   * 若开启免打扰模式，则声音提醒和震动提醒会失效，即收到消息时不会有震动和提示音
   *
   * @param conversation 会话id
   * @param message      收到的消息
   * @return true:开启， false：不开启
   */
  @Override
  public boolean needQuiet(YWConversation conversation, YWMessage message) {
//        if (conversation.getConversationType() == YWConversationType.Tribe){
//            return true;
//        }
    return needQuiet;
  }

  /**
   * 收到通知栏消息时是否震动提醒，该设置在没有开启免打扰模式的情况下才有效
   *
   * @param conversation 会话id
   * @param message      收到的消息
   * @return true：震动，false：不震动
   */
  @Override
  public boolean needVibrator(YWConversation conversation, YWMessage message) {
//        if (conversation.getConversationType() == YWConversationType.Tribe){
//            return false;
//        }
    return needVibrator;
  }


  /**
   * 收到通知栏消息时是否有声音提醒，该设置在没有开启免打扰模式的情况下才有效
   *
   * @param conversation 会话id
   * @param message      收到的消息
   * @return true：有提示音，false：没有提示音
   */
  @Override
  public boolean needSound(YWConversation conversation, YWMessage message) {
//        if (conversation.getConversationType() == YWConversationType.Tribe){
//            return false;
//        }
    return needSound;
  }

  /**
   * 收到消息时，自定义消息通知栏的提示文案
   *
   * @param conversation
   * @param message
   * @param totalUnReadCount
   * @return，如果返回空，则使SDK默认的文案格式
   */
  @Override
  public String getNotificationTips(YWConversation conversation, YWMessage message, int totalUnReadCount) {
    return null;
    //return "自定义文案，未读消息数为:" + totalUnReadCount;
  }

  /**
   * 收到消息时的自定义通知栏点击Intent
   *
   * @param conversation     收到消息的会话
   * @param message          收到的消息
   * @param totalUnReadCount 会话中消息未读数
   * @return 如果返回null，则使用全局自定义Intent
   */
  public Intent getCustomNotificationIntent(YWConversation conversation, YWMessage message, int totalUnReadCount) {
    //以下仅为示例代码，需要Intent开发者根据不同目的自己实现
//        Intent intent = new Intent(Intent.ACTION_MAIN);
//        intent.addCategory(Intent.CATEGORY_LAUNCHER);
//        intent.setClass(DemoApplication.getContext(), FragmentTabs.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        return intent;

    return null;
  }

  /**
   * 获取通知栏图标Icon
   *
   * @return ResId
   */
  @Override
  public int getNotificationIconResID() {
    return R.mipmap.ic_launcher;
  }

  /**
   * 获取通知栏显示Title
   *
   * @return
   */
  @Override
  public String getAppName() {
    return "Join";
  }
}
