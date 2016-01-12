package com.ikaowo.join.modules.push.processer;

import android.content.Context;
import com.ikaowo.join.BuildConfig;
import com.ikaowo.join.common.service.WebViewService;

/**
 * Created by weibo on 16-1-12.
 */
public class PromptionPassedProcesser extends PushDataProcesser {

  @Override public void action(Context context, int targetId) {
    String url = BuildConfig.PROMPTION_URL + targetId;

    WebViewService webViewService = jContext.getServiceByInterface(WebViewService.class);
    WebViewService.WebViewRequest request = new WebViewService.WebViewRequest();
    request.url = url;
    webViewService.viewPromptionDetail(context, targetId, request);
  }
}
