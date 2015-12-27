package com.common.framework.core;

import android.content.Context;
import android.content.Intent;

import com.common.framework.interceptor.JInterceptor;
import com.common.framework.manager.JAppInfoManager;
import com.common.framework.manager.JDeviceManager;
import com.common.framework.manager.JServiceManager;
import com.common.framework.manager.impl.JAppInfoManagerImpl;
import com.common.framework.manager.impl.JDeviceManagerImpl;
import com.common.framework.manager.impl.JServiceManagerImpl;
import com.common.framework.model.JServiceInfo;
import com.common.framework.util.JLog;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by weibo on 15-12-1.
 */
public class JContext {
  private static final String TAG = "JContext";

  private Context mContext;

  private JServiceManager mServiceManager;

  private JAppInfoManager mAppInfoManager;

  private JDeviceManager mDeviceManager;

  private Map<String, Object> mSavedData = new HashMap<String, Object>();

  public JContext(Context context) {
    mContext = context;
    mAppInfoManager = JAppInfoManagerImpl.getInstance(context);
    mServiceManager = JServiceManagerImpl.getInstance();
    mDeviceManager = JDeviceManagerImpl.getInstance(context);
    mSavedData.clear();
  }

  public boolean registerService(JServiceInfo serviceInfo) {
    return mServiceManager.registerService(serviceInfo);
  }

  public <T> T getServiceByInterface(
    Class<? extends JCommonService> serviceInterface) {
    return mServiceManager.getServiceByInterface(serviceInterface);
  }

  public void putData(String key, Object value) {
    mSavedData.put(key, value);
  }

  public Object getData(String key) {
    return mSavedData.get(key);
  }

  public boolean isDebuggable() {
    return mAppInfoManager.isDebuggable();
  }

  public void setDebuggable(boolean debuggable) {
    mAppInfoManager.setDebuggable(debuggable);
  }

  public String getDeviceId() {
    return mAppInfoManager.getDeviceId();
  }

  public String getChannleId() {
    return mAppInfoManager.getChannelId();
  }

  public int getScreenWidth() {
    return mDeviceManager.getDeviceScreenWidth();
  }

  public int getScreenHeight() {
    return mDeviceManager.getDeviceScreenHeight();
  }

  public String getVersionName() {
    return mAppInfoManager.getVersionName();
  }

  public void playSoundAndVibrate(int playCount) {
    mDeviceManager.playSoundAndVibrate(playCount);
  }

  public void stopVibrateAndMediaPlayer() {
    mDeviceManager.stopVibrateAndMediaPlayer();
  }

  public boolean startActivity(Context context, Intent intent) {
    try {
      String className = intent.getComponent().getClassName();
      if (className.endsWith("_")) {
        className = className.substring(0, className.length() - 1);
      }
      Class<?> c = Class.forName(className);
      JAction ac = c.getAnnotation(JAction.class);
      if (ac == null) {
        JLog.i(TAG, className + "没有拦截器,直接启动activity");
        context.startActivity(intent);
        return true;
      } else {
        Object o = ac.ai().newInstance();
        if (JInterceptor.class.isInstance(o)) {
          JInterceptor ai = (JInterceptor) o;
          return ai.check(context, intent);
        } else {
          JLog.e(TAG, className + "拦截器类型错误");
          return false;
        }
      }
    } catch (Exception e) {
      JLog.printStackTraceAndMore(e);
    }
    return false;
  }

  public void startActivityForResult(Context context, Intent intent, JCallback callback) {
    JExternalService service = getServiceByInterface(JExternalService.class);
    service.onStart(intent.getComponent().getClassName(), callback);
    if (!startActivity(context, intent)) {
      JLog.e(TAG, intent.getComponent().getClassName() + "启动失败");
    }
  }

  public File getDir(String name, int mode) {
    return mContext.getDir(name, mode);
  }

  public int dip2px(float dp) {
    return mDeviceManager.dip2px(dp);
  }

  public boolean isNetworkConnected() {
    return mDeviceManager.isNetworkConnected(mContext);
  }
}