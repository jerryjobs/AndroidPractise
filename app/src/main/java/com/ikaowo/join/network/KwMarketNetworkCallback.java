package com.ikaowo.join.network;

import com.common.framework.network.NetworkCallback;
import com.common.framework.util.JToast;
import com.ikaowo.join.model.base.BaseResponse;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;
import java.lang.annotation.Annotation;

import retrofit.Converter;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by weibo on 15-12-2.
 */
public abstract class KwMarketNetworkCallback<T> extends NetworkCallback<T> {

  @Override
  public void onFailed(Response response, Retrofit retrofit) {
    Converter<ResponseBody, BaseResponse> converter = retrofit.responseConverter(BaseResponse.class, new Annotation[0]);
    BaseResponse error;
    try {
      error = converter.convert(response.errorBody());
      JToast.toastShort(error.msg);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
