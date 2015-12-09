package com.common.framework.core;

import android.app.Activity;

/**
 * 自己实现类似startActivityForResult，
 * 可以在在非Activity里面调用
 * Created by weibo on 15-12-1.
 */
public abstract class JExternalService extends JCommonService {
    public abstract void onStart(String name, JCallback callback);

    public abstract <T> void onFinish(Activity activity, T t);

    public abstract <T> void onFinish(String activityName, T t);
}