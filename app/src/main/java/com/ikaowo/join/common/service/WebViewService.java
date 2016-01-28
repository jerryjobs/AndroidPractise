package com.ikaowo.join.common.service;

import android.content.Context;
import com.common.framework.core.JCommonService;

/**
 * Created by weibo on 7/5/15.
 */
public abstract class WebViewService extends JCommonService {

  public abstract void openWebView(Context context, WebViewRequest request);

  public abstract void viewPromptionDetail(Context context, int promptionId,
      WebViewRequest request);

  public static class WebViewRequest {
    public String url;
  }
}
