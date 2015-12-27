package com.common.framework.manager;

import android.content.Context;

/**
 * Created by weibo on 15-12-1.
 */
public abstract class JDeviceManager {

  public abstract int getDeviceScreenWidth();

  public abstract int getDeviceScreenHeight();

  public abstract int dip2px(float dp);

  public abstract void playSoundAndVibrate(int playCount);

  public abstract void stopVibrateAndMediaPlayer();

  public abstract boolean isNetworkConnected(Context context);

}