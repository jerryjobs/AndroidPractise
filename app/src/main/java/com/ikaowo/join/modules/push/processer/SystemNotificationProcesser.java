package com.ikaowo.join.modules.push.processer;

import android.content.Context;

import com.ikaowo.join.common.service.WebViewService;

/**
 * Created by weibo on 16-1-13.
 */
public class SystemNotificationProcesser extends PushDataProcesser {
    @Override
    public void openPage(Context context, String target, String targetUrl) {
        WebViewService webViewService = jContext.getServiceByInterface(WebViewService.class);
        WebViewService.WebViewRequest request = new WebViewService.WebViewRequest();
        request.url = targetUrl;

        webViewService.openWebView(context, request);
    }
}
