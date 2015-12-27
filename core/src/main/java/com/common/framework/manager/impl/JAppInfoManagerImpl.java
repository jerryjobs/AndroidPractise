package com.common.framework.manager.impl;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.common.framework.core.JApplication;
import com.common.framework.manager.JAppInfoManager;

/**
 * Created by weibo on 15-12-1.
 */
public class JAppInfoManagerImpl extends JAppInfoManager {

  private static JAppInfoManager mAppInfo;
  private boolean mDebuggable;
  private String mDeviceId;
  private String mChannelId;
  private String mVersionName;
  private boolean mDebugable;

  private JAppInfoManagerImpl(Context context) {
    initData(context);
  }

  public static JAppInfoManager getInstance(Context context) {
    if (mAppInfo == null) {
      mAppInfo = new JAppInfoManagerImpl(context);
    }
    return mAppInfo;
  }

  private void initData(Context context) {
    try {
      ApplicationInfo appInfo = context.getPackageManager()
        .getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
      if (appInfo.metaData != null) {
        mChannelId = appInfo.metaData.getString("UMENG_CHANNEL");
      }
    } catch (PackageManager.NameNotFoundException e) {
    }

    try {
      PackageManager manager = context.getPackageManager();
      PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
      mVersionName = info.versionName;
    } catch (PackageManager.NameNotFoundException e) {
    }

    try {
      mDeviceId = ((TelephonyManager) context
        .getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
    } catch (Exception e) {
    }
    try {
      if (TextUtils.isEmpty(mDeviceId)) {
        WifiManager wifi = (WifiManager) context
          .getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        mDeviceId = info.getMacAddress();
      }
    } catch (Exception e) {
    }
    if (TextUtils.isEmpty(mDeviceId)) {
      try {
        mDeviceId = Settings.Secure.getString(JApplication
            .getInstance().getContentResolver(),
          Settings.Secure.ANDROID_ID);
      } catch (Exception e) {
      }
    }
  }

  @Override
  public boolean isDebuggable() {
    return mDebugable;
  }

  @Override
  public void setDebuggable(boolean debuggable) {
    mDebugable = debuggable;
  }

  @Override
  public String getDeviceId() {
    return mDeviceId;
  }

  @Override
  public String getChannelId() {
    return mChannelId;
  }

  @Override
  public String getVersionName() {
    return mVersionName;
  }
}