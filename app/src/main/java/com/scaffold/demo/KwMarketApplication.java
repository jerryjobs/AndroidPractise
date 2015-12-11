package com.scaffold.demo;

import com.common.framework.core.JApplication;
import com.common.framework.core.JContext;
import com.common.framework.network.NetworkManager;
import com.scaffold.demo.network.TestInterface;

/**
 * Created by weibo on 15-12-1.
 */
public class KwMarketApplication extends JApplication {

    @Override
    public void registerService(JContext context) {

    }

    @Override
    public void registerNetworkService(NetworkManager networkManager) {
        networkManager.registerService(TestInterface.class);
    }

    @Override
    public String getBaseUrl() {
        return "https://api.test.ikaowo.com/";
    }
}
