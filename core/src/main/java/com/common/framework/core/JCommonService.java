package com.common.framework.core;

/**
 * Created by weibo on 15-12-1.
 */
public abstract class JCommonService {
    /**
     * 服务创建时调用
     */
    public abstract void onCreate();

    /**
     * 服务销毁时调用
     */
    public abstract void onDestroy();
}

