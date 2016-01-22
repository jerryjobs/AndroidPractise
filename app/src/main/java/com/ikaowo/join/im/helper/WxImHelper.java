package com.ikaowo.join.im.helper;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import com.alibaba.mobileim.IYWLoginService;
import com.alibaba.mobileim.YWLoginParam;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.common.framework.core.JDialogHelper;
import com.ikaowo.join.R;

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

  public void initWxService(final Context context, String password, String userid) {

    LoginHelper loginHelper = LoginHelper.getInstance();
    IYWLoginService loginService = loginHelper.getIMKit().getLoginService();
    YWLoginParam loginParam = YWLoginParam.createLoginParam(userid, password);
    loginService.login(loginParam, new IWxCallback() {

      @Override public void onSuccess(Object... objects) {
      }

      @Override public void onError(int i, String s) {
        new JDialogHelper(context).showConfirmDialog(R.string.hint_wx_service_failed);
      }

      @Override public void onProgress(int i) {
      }
    });
  }
}
