package com.common.framework.manager.impl;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import com.common.framework.manager.JDeviceManager;
import com.common.framework.util.JLog;
import java.io.IOException;

/**
 * Created by weibo on 15-12-1.
 */
public class JDeviceManagerImpl extends JDeviceManager {

  private static JDeviceManager mDeviceManager;
  private int mDeviceWidth, mDeviceHeight;
  private float scale;
  private Vibrator mVibrator;
  private MediaPlayer mMediaPlayer;
  private Context mContext;

  private JDeviceManagerImpl(Context context) {
    DisplayMetrics dm = context.getResources().getDisplayMetrics();
    mContext = context;
    mDeviceWidth = dm.widthPixels;
    mDeviceHeight = dm.heightPixels;
    scale = dm.density;
  }

  public static JDeviceManager getInstance(Context context) {
    if (mDeviceManager == null) {
      mDeviceManager = new JDeviceManagerImpl(context);
    }

    return mDeviceManager;
  }

  @Override public int getDeviceScreenWidth() {
    return mDeviceWidth;
  }

  @Override public int getDeviceScreenHeight() {
    return mDeviceHeight;
  }

  @Override public int dip2px(float dp) {
    return (int) (scale * dp + 0.5f);
  }

  /**
   * 响铃和震动
   *
   * @param playCount 0为无限循环，-1为只响铃一次
   */
  @Override public void playSoundAndVibrate(int playCount) {
    mVibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
    if (playCount != -1) {
      mVibrator.vibrate(new long[] { 1000, 10, 100, 1000 }, playCount);
      JLog.e("ee", "正在响铃");
      // 使用来电铃声的铃声路径
      //			/**创建MediaPlayer对象**/
      //		    MediaPlayer mMediaPlayer = MediaPlayer.create(mContext, R.raw.v3);
      Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
      // 如果为空，才构造，不为空，说明之前有构造过
      if (mMediaPlayer == null) mMediaPlayer = new MediaPlayer();
      try {
        mMediaPlayer.setDataSource(mContext, uri);
        mMediaPlayer.setLooping(true); //循环播放
        mMediaPlayer.prepare();
        mMediaPlayer.start();
      } catch (IllegalArgumentException e) {
        e.printStackTrace();
      } catch (SecurityException e) {
        e.printStackTrace();
      } catch (IllegalStateException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      }
    } else {
      mVibrator.vibrate(new long[] { 1000 }, playCount);
    }
  }

  @Override public void stopVibrateAndMediaPlayer() {
    try {
      if (mVibrator != null) {
        mVibrator.cancel();
        mVibrator = null;
      }

      if (mMediaPlayer != null) {
        mMediaPlayer.release();
        mMediaPlayer = null;
      }
    } catch (IllegalStateException e) {
      e.printStackTrace();
    }
  }

  @Override public boolean isNetworkConnected(Context context) {
    ConnectivityManager connectivityManager =
        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    if (connectivityManager != null) {
      NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
      if (networkInfo != null && networkInfo.isConnected()) {
        return true;
      }
    }
    return false;
  }
}
