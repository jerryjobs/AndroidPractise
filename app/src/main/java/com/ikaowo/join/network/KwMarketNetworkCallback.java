package com.ikaowo.join.network;

import android.content.Context;
import android.os.Handler;
import com.common.framework.core.JApplication;
import com.common.framework.network.NetworkCallback;
import com.common.framework.util.JToast;
import com.ikaowo.join.R;
import com.ikaowo.join.common.service.UserService;
import com.ikaowo.join.model.base.BaseResponse;
import com.squareup.okhttp.ResponseBody;
import com.umeng.analytics.MobclickAgent;
import java.lang.annotation.Annotation;
import retrofit.Converter;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by weibo on 15-12-2.
 */
public abstract class KwMarketNetworkCallback<T> extends NetworkCallback<T> {
  private Context context;

  public KwMarketNetworkCallback(Context context) {
    super(context);
    this.context = context;
  }

  @Override public void onFailed(Response response, Retrofit retrofit) {
    Converter<ResponseBody, BaseResponse> converter =
        retrofit.responseConverter(BaseResponse.class, new Annotation[0]);
    BaseResponse error;
    try {
      error = converter.convert(response.errorBody());
      JToast.toastShort(error.msg);
    } catch (Exception e) {
      JToast.toastLong("服务器开小差了，请重试");
      MobclickAgent.reportError(JApplication.getInstance().getApplicationContext(),
          "ONREQUREST_FAILED:" + retrofit.baseUrl() + ":" +  e.getMessage());
    }

    if (response.code() == 401) {
      new Handler().postDelayed(new Runnable() {
        @Override public void run() {
          UserService userService =
              JApplication.getJContext().getServiceByInterface(UserService.class);
          userService.logout(context);
        }
      }, 600);
    }
  }
}
