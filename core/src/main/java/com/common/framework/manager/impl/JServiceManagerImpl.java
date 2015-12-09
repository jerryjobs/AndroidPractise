package com.common.framework.manager.impl;

import com.common.framework.core.JCommonService;
import com.common.framework.manager.JServiceManager;
import com.common.framework.model.JServiceInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by weibo on 15-12-1.
 */
public class JServiceManagerImpl implements JServiceManager {

    private static JServiceManager mServiceManager;

    private static Map<Class<? extends JCommonService>, JCommonService> mServiceMap;

    private JServiceManagerImpl() {
        mServiceMap = new HashMap<Class<? extends JCommonService>, JCommonService>();
    }

    public static JServiceManager getInstance() {
        if (mServiceManager == null) {
            mServiceManager = new JServiceManagerImpl();
        }

        return mServiceManager;
    }

    @Override
    public boolean registerService(JServiceInfo serviceInfo) {
        Class<? extends JCommonService> service = serviceInfo.getServiceInterface();
        JCommonService serviceImpl = serviceInfo.getServiceImpl();

        if (service == null || serviceImpl == null) {
            return false;
        }

        if (!mServiceMap.containsKey(service)) {
            mServiceMap.put(service, serviceImpl);
            serviceImpl.onCreate();

            return true;
        }

        return false;
    }

    @Override
    public <T> T getServiceByInterface(Class<? extends JCommonService> serviceInterface) {
        return (T) mServiceMap.get(serviceInterface);
    }
}