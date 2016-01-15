package com.ikaowo.join.modules.push.processer;

import android.content.Context;
import com.ikaowo.join.BuildConfig;
import com.ikaowo.join.common.service.WebViewService;

/**
 * Created by weibo on 16-1-12.
 */
public class PromptionPassedProcesser extends PushDataProcesser {

  @Override public void openPage(Context context, String target, String targetUrl) {
    String url = BuildConfig.PROMPTION_URL + target;

    WebViewService webViewService = jContext.getServiceByInterface(WebViewService.class);
    WebViewService.WebViewRequest request = new WebViewService.WebViewRequest();
    request.url = url;
    webViewService.viewPromptionDetail(context, Integer.valueOf(target), request);
  }
}
