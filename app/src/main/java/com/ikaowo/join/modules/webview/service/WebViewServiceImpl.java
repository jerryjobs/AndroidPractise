package com.ikaowo.join.modules.webview.service;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.common.framework.core.JApplication;
import com.ikaowo.join.common.service.UserService;
import com.ikaowo.join.common.service.WebViewService;
import com.ikaowo.join.modules.webview.activity.PromptionDetailWebViewActivity;
import com.ikaowo.join.modules.webview.activity.WebViewActivity;
import com.ikaowo.join.util.Constant;

/**
 * Created by weibo on 15-12-25.
 */
public class WebViewServiceImpl extends WebViewService {
  @Override public void openWebView(Context context, WebViewRequest request) {
    if (TextUtils.isEmpty(request.url)) {
      return;
    }
    Intent intent = new Intent(context, WebViewActivity.class);
    intent.putExtra(Constant.URL, request.url);
    JApplication.getJContext().startActivity(context, intent);
  }

  @Override
  public void viewPromptionDetail(Context context, UserService userService, int promptionId,
      WebViewRequest request) {

    if (TextUtils.isEmpty(request.url)) {
      return;
    }

    Intent intent = new Intent(context, PromptionDetailWebViewActivity.class);
    intent.putExtra(Constant.PROMPTION_ID, promptionId);
    intent.putExtra(Constant.URL, request.url);
    JApplication.getJContext().startActivity(context, intent);
  }

  @Override public void onCreate() {

  }

  @Override public void onDestroy() {

  }
}
