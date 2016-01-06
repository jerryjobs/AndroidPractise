package com.ikaowo.join.im.helper;

import android.app.Application;
import android.content.Context;

import com.alibaba.mobileim.YWAPI;
import com.alibaba.mobileim.YWIMCore;
import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.login.IYWConnectionListener;
import com.alibaba.mobileim.login.YWLoginCode;
import com.ikaowo.join.eventbus.WxKickedOffCallback;

import de.greenrobot.event.EventBus;

/**
 * Created by weibo on 15-12-30.
 */
public class LoginHelper {
  private static LoginHelper instance = new LoginHelper();
  private final String APP_KEY = "23283999";
//  private final String APP_KEY = "23015524"; //demo
  private YWIMKit imKit;
  private Context context;
  private YWConnectionListenerImpl mYWConnectionListenerImpl = new YWConnectionListenerImpl();

  private LoginHelper() {}

  public static LoginHelper getInstance() {
    return instance;
  }

  public void initSDK(Application context) {
    imKit = YWAPI.getIMKitInstance();
    YWAPI.init(context, APP_KEY);

    this.context = context;
    NotificationInitHelper.init();

    addConnectionListener();
  }

  private void addConnectionListener() {
    if (imKit == null) {
      return;
    }

    YWIMCore imCore = imKit.getIMCore();
    imCore.removeConnectionListener(mYWConnectionListenerImpl);
    imCore.addConnectionListener(mYWConnectionListenerImpl);
  }

  private class YWConnectionListenerImpl implements IYWConnectionListener {

    @Override
    public void onReConnecting() {

    }

    @Override
    public void onReConnected() {

//				YWLog.i("LoginSampleHelper", "onReConnected");


    }

    @Override
    public void onDisconnect(int arg0, String arg1) {
      if (arg0 == YWLoginCode.LOGON_FAIL_KICKOFF) {
        EventBus.getDefault().post(new WxKickedOffCallback() {
          @Override
          public boolean kickedOff() {
            return true;
          }
        });
      }
    }
  }

  public YWIMKit getIMKit() {
    return imKit;
  }
}
