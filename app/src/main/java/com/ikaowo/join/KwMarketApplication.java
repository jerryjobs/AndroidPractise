package com.ikaowo.join;

import com.alibaba.wxlib.util.SysUtil;
import com.common.framework.core.JApplication;
import com.common.framework.core.JContext;
import com.common.framework.model.JServiceInfo;
import com.common.framework.network.NetworkManager;
import com.ikaowo.join.common.service.BrandService;
import com.ikaowo.join.common.service.MineService;
import com.ikaowo.join.common.service.NotificationService;
import com.ikaowo.join.common.service.PromptionService;
import com.ikaowo.join.common.service.UserService;
import com.ikaowo.join.common.service.WebViewService;
import com.ikaowo.join.im.helper.InitHelper;
import com.ikaowo.join.modules.brand.service.BrandServiceImpl;
import com.ikaowo.join.modules.mine.service.MineServiceImpl;
import com.ikaowo.join.modules.mine.service.NotificationServiceImpl;
import com.ikaowo.join.modules.promption.service.PromptionServiceImpl;
import com.ikaowo.join.modules.user.service.UserServiceImpl;
import com.ikaowo.join.modules.webview.service.WebViewServiceImpl;
import com.ikaowo.join.network.BrandInterface;
import com.ikaowo.join.network.CommonInterface;
import com.ikaowo.join.network.NotificationInterface;
import com.ikaowo.join.network.PromptionInterface;
import com.ikaowo.join.network.PushInterface;
import com.ikaowo.join.network.QiniuInterface;
import com.ikaowo.join.network.TestInterface;
import com.ikaowo.join.network.UserInterface;
import com.ikaowo.join.network.VerifyCodeInterface;

/**
 * Created by weibo on 15-12-1.
 */
public class KwMarketApplication extends JApplication {
  @Override public void onCreate() {
    prdEnv = !BuildConfig.DEBUG;
    super.onCreate();
    //Application.onCreate中，首先执行这部分代码, 因为，如果在":TCMSSevice"进程中，无需进行openIM和app业务的初始化，以节省内存
    //todo 特别注意:这段代码不能封装到其他方法中，必须在onCreate顶层代码中!
    //以下代码固定在此处，不要改动
    SysUtil.setApplication(this);
    if (SysUtil.isTCMSServiceProcess(this)) {
      return;  //todo 特别注意：此处return是退出onCreate函数，因此不能封装到其他任何方法中!
    }
    //以上代码固定在这个位置，不要改动
    InitHelper.initYWSDK(this);
  }

  @Override public void registerService(JContext context) {
    context.registerService(
        JServiceInfo.createServiceInfo(UserService.class, new UserServiceImpl()));
    context.registerService(
        JServiceInfo.createServiceInfo(PromptionService.class, new PromptionServiceImpl()));
    context.registerService(
        JServiceInfo.createServiceInfo(WebViewService.class, new WebViewServiceImpl()));
    context.registerService(
        JServiceInfo.createServiceInfo(BrandService.class, new BrandServiceImpl()));
    context.registerService(
        JServiceInfo.createServiceInfo(MineService.class, new MineServiceImpl()));
    context.registerService(
        JServiceInfo.createServiceInfo(NotificationService.class, new NotificationServiceImpl()));
  }

  @Override public void registerNetworkService(NetworkManager networkManager) {
    networkManager.registerService(TestInterface.class);
    networkManager.registerService(QiniuInterface.class);
    networkManager.registerService(BrandInterface.class);
    networkManager.registerService(VerifyCodeInterface.class);
    networkManager.registerService(UserInterface.class);
    networkManager.registerService(PromptionInterface.class);
    networkManager.registerService(CommonInterface.class);
    networkManager.registerService(NotificationInterface.class);
    networkManager.registerService(PushInterface.class);
  }

  @Override public String getBaseUrl() {
    return "http://mars.test.ikaowo.com/";
  }
}
