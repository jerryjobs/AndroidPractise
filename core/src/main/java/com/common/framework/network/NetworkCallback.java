package com.common.framework.network;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by weibo on 15-12-2.
 */
public abstract class NetworkCallback<T> implements Callback<T> {

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

    }

    public abstract void onSuccess(T t);

}
