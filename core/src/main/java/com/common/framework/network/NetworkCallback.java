package com.common.framework.network;

import com.common.framework.model.JErrorReponse;
import com.common.framework.util.JToast;
import com.common.framework.widget.listview.RecyclerViewHelper;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;
import java.lang.annotation.Annotation;

import retrofit.Callback;
import retrofit.Converter;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by weibo on 15-12-2.
 */
public abstract class NetworkCallback<T> implements Callback<T> {

    private RecyclerViewHelper.Action action;

    @Override
    public void onResponse(Response<T> response, Retrofit retrofit) {
        if (response.isSuccess()) {
            onSuccess(response.body());
        } else {
            onFailed(response, retrofit);
        }
    }

    @Override
    public void onFailure(Throwable t) {

    }

    //根据对应的项目，对错误做一些处理，在1.x版本，有一个errorhandler,
    //在2.x版本里面，实现一个默认的onFailed方法来替代大部分errorHandler的工作
    public void onFailed(Response response, Retrofit retrofit) {
        Converter<ResponseBody, JErrorReponse> errorConverter
                = retrofit.responseConverter(JErrorReponse.class, new Annotation[0]);
        try {
            JErrorReponse error = errorConverter.convert(response.errorBody());
            if (error != null) {
                JToast.toastLong(error.msg);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setAction(RecyclerViewHelper.Action action) {
        this.action = action;
    }

    public RecyclerViewHelper.Action getAction() {
        return this.action;
    }

    public abstract void onSuccess(T t);

}
