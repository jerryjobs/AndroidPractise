package com.ikaowo.join.im.helper;

/**
 * Created by weibo on 15-12-30.
 */
public class WxImHelper {

  private static WxImHelper instance = new WxImHelper();
  private WxImHelper() {

  }

  public static WxImHelper getInstance() {
    return instance;
  }
}
