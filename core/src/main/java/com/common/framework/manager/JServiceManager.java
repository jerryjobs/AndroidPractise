package com.common.framework.manager;

import com.common.framework.core.JCommonService;
import com.common.framework.model.JServiceInfo;

/**
 * Created by weibo on 15-12-1.
 */
public interface JServiceManager {

  /**
   * 注册服务
   *
   * @param serviceInfo 服务信息
   * @return
   */
  boolean registerService(JServiceInfo serviceInfo);

  <T> T getServiceByInterface(Class<? extends JCommonService> serviceInterface);
}