package com.ikaowo.join.modules.webview.activity;

import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.ZoomButtonsController;

import com.common.framework.core.JApplication;
import com.ikaowo.join.common.service.UserService;

import java.lang.reflect.Method;

/**
 * Created by weibo on 11/5/15.
 */
public class WebViewHelper {

    private WebView mWebView;
    private ProgressBar mProgressbar;
    private String mUrl;
    private WebViewInterface mWebViewInterface;
    //约定好的名字
    private String interfaceName = "webViewControl";
    private UserService userService =
            JApplication.getJContext().getServiceByInterface(UserService.class);

    public WebViewHelper(String url, WebView webview, ProgressBar progressBar) {
        this.mWebView = webview;
        this.mProgressbar = progressBar;
        this.mUrl = getCompleteUrl(url, true);
        Log.d("WebViewHelper", "the complete url:" + this.mUrl);
    }

    public void setWebViewInterface(WebViewInterface webViewInterface) {
        this.mWebViewInterface = webViewInterface;
    }

    /**
     * 设置javascript支持
     */
    public void setJavascriptInterface(Object object) {

        if (mWebView == null) {
            return;
        }
        mWebView.addJavascriptInterface(object, interfaceName);
    }

    public void init() {
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setDomStorageEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setDatabaseEnabled(true);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            settings.setAppCacheMaxSize(5 * 1024 * 1024);
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            settings.setDatabasePath(JApplication.getInstance().getApplicationContext().getFilesDir().getPath() + mWebView.getContext().getPackageName() + "/databases/");
        }

        //去掉缩放按钮
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            // Use the API 11+ calls to disable the controls
            settings.setBuiltInZoomControls(true);
            settings.setDisplayZoomControls(false);
        } else {
            // Use the reflection magic to make it work on earlier APIs
            getControlls();
        }
        mWebView.setWebViewClient(new CustomWebViewClient());

        /**
         * 设置下载监听，当url为文件时候开始下载文件
         */
        mWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition,
                                        String mimetype, long contentLength) {
                //				download(url);
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (mWebViewInterface != null) {
                    mWebViewInterface.setWebViewTitle(title);
                }
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (mProgressbar != null) {
                    if (newProgress == 100) {
                        mProgressbar.setVisibility(View.GONE);
                    } else {
                        if (mProgressbar.getVisibility() == View.GONE)
                            mProgressbar.setVisibility(View.VISIBLE);
                        mProgressbar.setProgress(newProgress);
                    }
                }
                super.onProgressChanged(view, newProgress);
            }
        });
        mWebView.loadUrl(mUrl);
    }

    /**
     * This is where the magic happens :D
     */
    private void getControlls() {
        try {
            Class webview = Class.forName("android.webkit.WebView");
            Method method = webview.getMethod("getZoomButtonsController");
            ZoomButtonsController zoom_controll = (ZoomButtonsController) method.invoke(this, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onDestroy(ViewGroup webViewLayout) {
        if (mWebView != null) {
            //解决webview关闭时异常
            mWebView.setVisibility(View.GONE);
            webViewLayout.removeView(mWebView);
            mWebView.removeAllViews();
            mWebView.destroy();
        }
    }

    public String getCompleteUrl(String url, boolean isApp) {
        StringBuilder sb = new StringBuilder();
        if (url.indexOf("?") == -1) {
            sb.append(url).append("?isApp=" + isApp);
        } else {
            sb.append(url).append("&isApp=" + isApp);
        }
        if (userService.isLogined()) {
            sb.append("&companyid=" + userService.getUserCompanyId());
        }
        sb.append("&uid=")
                .append(userService.getUserId() + "")
                .append("&version=")
                .append(JApplication.getJContext().getVersionName());

        return sb.toString();
    }

    public interface WebViewInterface {
        void setWebViewTitle(String title);

        boolean shouldOverrideUrlLoading(WebView view, String url);
    }

    class CustomWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (mWebViewInterface != null) {
                return mWebViewInterface.shouldOverrideUrlLoading(view, url);
            }

            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description,
                                    String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
        }
    }
}


