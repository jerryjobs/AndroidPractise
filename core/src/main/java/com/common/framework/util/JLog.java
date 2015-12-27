package com.common.framework.util;

/**
 * Created by weibo on 15-12-1.
 */

import android.util.Log;

import com.common.framework.core.JApplication;

public class JLog {

  public static void d(String tag, String msg) {
    if (isDebuggable()) {
      Log.d(tag, msg);
    }
  }

  public static void e(String tag, String msg) {
    if (isDebuggable()) {
      Log.e(tag, msg);
    }
  }

  public static void i(String tag, String msg) {
    if (isDebuggable()) {
      Log.i(tag, msg);
    }
  }

  public static void v(String tag, String msg) {
    if (isDebuggable()) {
      Log.v(tag, msg);
    }
  }

  public static void w(String tag, String msg) {
    if (isDebuggable()) {
      Log.w(tag, msg);
    }
  }

  public static void printStackTraceAndMore(Throwable e) {
    if (isDebuggable()) {
      e.printStackTrace();
    }
  }

  private static boolean isDebuggable() {
    return JApplication.getJContext().isDebuggable();
  }

}