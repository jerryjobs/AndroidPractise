package com.ikaowo.join.modules.push.processer;

import android.content.Context;
import android.text.TextUtils;
import com.ikaowo.join.common.service.WebViewService;

/**
 * Created by weibo on 16-1-13.
 */
public class SystemNotificationProcesser extends PushDataProcesser {
  @Override public void action(Context context, String target) {
    WebViewService webViewService = jContext.getServiceByInterface(WebViewService.class);
    WebViewService.WebViewRequest request = new WebViewService.WebViewRequest();
    request.url = target;
    if (TextUtils.isEmpty(target)) {
      webViewService.openWebView(context, request);
    } else {
      webViewService.viewPromptionDetail(context, Integer.valueOf(target), request);
    }
  }
}
