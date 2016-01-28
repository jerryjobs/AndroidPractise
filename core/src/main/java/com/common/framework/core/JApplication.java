package com.common.framework.core;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import com.common.framework.image.ImageLoader;
import com.common.framework.network.NetworkManager;

/**
 * Created by weibo on 15-12-1.
 */
public abstract class JApplication extends MultiDexApplication {

  private static JApplication application;
  private static JContext context;
  private static NetworkManager networkManager;
  private static ImageLoader imageLoader;

  public static JApplication getInstance() {
    return application;
  }

  public static JContext getJContext() {
    return context;
  }

  public static NetworkManager getNetworkManager() {
    return networkManager;
  }

  public static ImageLoader getImageLoader() {
    return imageLoader;
  }

  @Override public void onCreate() {
    super.onCreate();

    application = this;
    context = new JContext(this);
    networkManager = new NetworkManager(getApplicationContext(), getBaseUrl());
    imageLoader = new ImageLoader(getApplicationContext());

    registerService(context);
    registerNetworkService(networkManager);
  }

  @Override protected void attachBaseContext(Context base) {
    super.attachBaseContext(base);
    MultiDex.install(this);
  }

  //注册app 服务
  public abstract void registerService(JContext context);

  //注册app网络请求服务
  public abstract void registerNetworkService(NetworkManager networkManager);

  public abstract String getBaseUrl();
}
