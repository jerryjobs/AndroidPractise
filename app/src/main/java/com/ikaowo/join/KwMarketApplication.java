package com.ikaowo.join;

import com.common.framework.core.JApplication;
import com.common.framework.core.JContext;
import com.common.framework.network.NetworkManager;
import com.ikaowo.join.network.QiniuInterface;
import com.ikaowo.join.network.TestInterface;

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
        networkManager.registerService(QiniuInterface.class);
    }

    @Override
    public String getBaseUrl() {
        return "http://mars.test.ikaowo.com/";
    }
}
