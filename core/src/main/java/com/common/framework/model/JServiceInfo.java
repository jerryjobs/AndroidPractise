package com.common.framework.model;

import com.common.framework.core.JCommonService;

/**
 * Created by weibo on 15-12-1.
 */
public class JServiceInfo {
  private Class<? extends JCommonService> mServiceInterface; // 服务接口
  private JCommonService mServiceImpl; //服务具体实现类


  public static JServiceInfo createServiceInfo(
    Class<? extends JCommonService> serviceInterface, JCommonService serviceImpl) {
    JServiceInfo serviceInfo = new JServiceInfo();
    serviceInfo.mServiceInterface = serviceInterface;
    serviceInfo.mServiceImpl = serviceImpl;

    return serviceInfo;
  }

  public Class<? extends JCommonService> getServiceInterface() {
    return mServiceInterface;
  }

  public JCommonService getServiceImpl() {
    return mServiceImpl;
  }
}
