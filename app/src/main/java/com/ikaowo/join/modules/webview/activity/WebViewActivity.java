package com.ikaowo.join.modules.webview.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.ikaowo.join.BaseActivity;
import com.ikaowo.join.R;
import com.ikaowo.join.util.Constant;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by weibo on 15-12-25.
 */

public class WebViewActivity extends BaseActivity implements WebViewHelper.WebViewInterface {

  @Bind(R.id.webview)
  WebView webView;

  @Bind(R.id.webview_container_layout)
  ViewGroup webviewContainerLayout;

  @Bind(R.id.webview_progress)
  ProgressBar progressBar;

  protected String url;
  protected WebViewHelper webViewHelper;
  protected Intent intent;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_webview);
    ButterKnife.bind(this);

    toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    ActionBar ab = getSupportActionBar();
    ab.setDisplayHomeAsUpEnabled(true);

    getIntentData();
    setupView();
  }

  protected void getIntentData() {
    intent = getIntent();
    if (intent.getExtras() != null) {
      url = intent.getStringExtra(Constant.URL);
    }
  }

  private void setupView() {
    webViewHelper = new WebViewHelper(url, webView, progressBar);
    webViewHelper.setJavascriptInterface(this);

    webViewHelper.setWebViewInterface(this);
    webViewHelper.init();
  }

  @Override
  protected String getTag() {
    return "WebViewActivity";
  }

  @Override
  public void setWebViewTitle(String title) {
    toolbar.setTitle(title);
  }

  @Override
  public boolean shouldOverrideUrlLoading(WebView view, String url) {
    if (url == null) {
      return false;
    }

    if (url.startsWith("join://")) {
      view.getContext().startActivity(
        new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
      return true;
    } else if (url.startsWith("tel:")) {
      Intent intent = new Intent(Intent.ACTION_DIAL);
      intent.setData(Uri.parse(url));
      if (intent.resolveActivity(getPackageManager()) != null) {
        startActivity(intent);
      }
      return true;
    }
    return false;
  }

  @Override
  protected void onDestroy() {
    if (webViewHelper != null) {
      webViewHelper.onDestroy(webviewContainerLayout);
    }
    super.onDestroy();
  }
}
