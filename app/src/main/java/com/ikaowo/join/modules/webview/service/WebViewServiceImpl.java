package com.ikaowo.join.modules.webview.service;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.common.framework.core.JApplication;
import com.ikaowo.join.common.service.UserService;
import com.ikaowo.join.common.service.WebViewService;
import com.ikaowo.join.model.Promption;
import com.ikaowo.join.modules.webview.activity.PromptionDetailWebViewActivity;
import com.ikaowo.join.modules.webview.activity.WebViewActivity;
import com.ikaowo.join.util.Constant;

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
    intent.putExtra(Constant.URL, request.url);
    JApplication.getJContext().startActivity(context, intent);
  }

  @Override
  public void viewPromptionDetail(Context context, UserService userService, Promption promption, WebViewRequest request) {

    if (TextUtils.isEmpty(request.url)) {
      return;
    }
    String title = promption.title;
    String summary = promption.content;
    String imgUrl = promption.background;

    Intent intent = new Intent(context, PromptionDetailWebViewActivity.class);
    intent.putExtra(Constant.URL, request.url);
    intent.putExtra(Constant.SHAREW_TITLE, title);
    intent.putExtra(Constant.SHAREW_SUMMARY, summary);
    intent.putExtra(Constant.SHAREW_IMG_URL, imgUrl);

    boolean promptionShowEdit = Constant.PROMPTION_STATE_FAILED.equalsIgnoreCase(promption.state)
      || Constant.PROMPTION_STATE_NEW.equalsIgnoreCase(promption.state);

    int showOptionMenu;
    if (userService.isLogined()) {
      if (userService.getUserId() == promption.publishUid) {
        if (promptionShowEdit) {
          showOptionMenu = Constant.EDIT;
        } else {
          showOptionMenu = Constant.SHARE;
        }
      } else {
        if (promptionShowEdit) {
          showOptionMenu = Constant.NONE;
        } else {
          showOptionMenu = Constant.SHARE;
        }
      }
    } else {
      if (promptionShowEdit) { //
        showOptionMenu = Constant.NONE;
      } else {
        showOptionMenu = Constant.SHARE;
      }
    }
    intent.putExtra(Constant.PROMPTION_ID, promption.id);
    intent.putExtra(Constant.SHOW_OPTION_MENU, showOptionMenu);
    JApplication.getJContext().startActivity(context, intent);
  }

  @Override
  public void onCreate() {

  }

  @Override
  public void onDestroy() {

  }
}
