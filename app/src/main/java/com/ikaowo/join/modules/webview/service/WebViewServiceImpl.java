package com.ikaowo.join.modules.webview.service;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.common.framework.core.JApplication;
import com.ikaowo.join.common.service.WebViewService;
import com.ikaowo.join.modules.webview.activity.WebViewActivity;

/**
 * Created by weibo on 15-12-25.
 */
public class WebViewServiceImpl extends WebViewService {
  @Override
  public void openWebView(Context context, WebViewRequest request) {
    if (TextUtils.isEmpty(request.url)) {
      return;
    }
    Intent intent = new Intent(context, WebViewActivity.class);
    intent.putExtra("url", request.url);
    JApplication.getJContext().startActivity(context, intent);
  }

  @Override
  public void onCreate() {

  }

  @Override
  public void onDestroy() {

  }
}
