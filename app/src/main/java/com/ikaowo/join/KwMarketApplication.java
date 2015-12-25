package com.ikaowo.join;

import com.common.framework.core.JApplication;
import com.common.framework.core.JContext;
import com.common.framework.model.JServiceInfo;
import com.common.framework.network.NetworkManager;
import com.ikaowo.join.common.service.PromptionService;
import com.ikaowo.join.common.service.UserService;
import com.ikaowo.join.common.service.WebViewService;
import com.ikaowo.join.modules.promption.service.PromptionServiceImpl;
import com.ikaowo.join.modules.user.service.UserServiceImpl;
import com.ikaowo.join.modules.webview.service.WebViewServiceImpl;
import com.ikaowo.join.network.BrandInterface;
import com.ikaowo.join.network.PromptionInterface;
import com.ikaowo.join.network.QiniuInterface;
import com.ikaowo.join.network.TestInterface;
import com.ikaowo.join.network.UserInterface;
import com.ikaowo.join.network.VerifyCodeInterface;

/**
 * Created by weibo on 15-12-1.
 */
public class KwMarketApplication extends JApplication {

  @Override
  public void registerService(JContext context) {
    context.registerService(JServiceInfo.createServiceInfo(UserService.class, new UserServiceImpl()));
    context.registerService(JServiceInfo.createServiceInfo(PromptionService.class, new PromptionServiceImpl()));
    context.registerService(JServiceInfo.createServiceInfo(WebViewService.class, new WebViewServiceImpl()));
  }

  @Override
  public void registerNetworkService(NetworkManager networkManager) {
    networkManager.registerService(TestInterface.class);
    networkManager.registerService(QiniuInterface.class);
    networkManager.registerService(BrandInterface.class);
    networkManager.registerService(VerifyCodeInterface.class);
    networkManager.registerService(UserInterface.class);
    networkManager.registerService(PromptionInterface.class);
  }

  @Override
  public String getBaseUrl() {
    return "http://mars.test.ikaowo.com/";
  }
}
