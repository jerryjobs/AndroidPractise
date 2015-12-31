package com.ikaowo.join.common.service;

import android.content.Context;

import com.common.framework.core.JCommonService;

import java.io.Serializable;

/**
 * Created by weibo on 7/5/15.
 */
public abstract class WebViewService extends JCommonService {

  public abstract void openWebView(Context context, WebViewRequest request);

  public abstract void viewPromptionDetail(Context context, String title, String summary, String imgUrl, int showOptionMenu, WebViewRequest request);

  public static class WebViewRequest {
    public String url;
  }

}
