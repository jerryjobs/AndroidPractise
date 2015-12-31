package com.ikaowo.join.im.helper;

import android.app.Application;

import com.alibaba.mobileim.YWAPI;
import com.alibaba.mobileim.YWIMKit;

/**
 * Created by weibo on 15-12-30.
 */
public class LoginHelper {
  private static LoginHelper instance = new LoginHelper();
  private final String APP_KEY = "23283999";
//  private final String APP_KEY = "23015524"; //demo
  private YWIMKit imKit;

  private LoginHelper() {}

  public static LoginHelper getInstance() {
    return instance;
  }

  public void initSDK(Application context) {
    imKit = YWAPI.getIMKitInstance();
    YWAPI.init(context, APP_KEY);

    NotificationInitHelper.init();
  }

  public YWIMKit getIMKit() {
    return imKit;
  }
}
