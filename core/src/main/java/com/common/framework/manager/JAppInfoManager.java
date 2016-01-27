package com.common.framework.manager;

/**
 * Created by weibo on 15-12-1.
 */
public abstract class JAppInfoManager {

    public abstract boolean isDebuggable();

    public abstract void setDebuggable(boolean debuggable);

    public abstract String getDeviceId();

    public abstract String getChannelId();

    public abstract String getVersionName();
}
