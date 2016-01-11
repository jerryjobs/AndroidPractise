package com.ikaowo.join.modules.mine.service;

import android.content.Context;
import android.content.Intent;

import com.common.framework.core.JApplication;
import com.ikaowo.join.common.service.NotificationService;
import com.ikaowo.join.modules.mine.activity.SystemNotificationActivity;

/**
 * Created by weibo on 16-1-11.
 */
public class NotificationServiceImpl extends NotificationService {

  @Override
  public void onCreate() {

  }

  @Override
  public void onDestroy() {

  }

  @Override
  public void viewNotification(Context context) {
    Intent intent = new Intent(context, SystemNotificationActivity.class);
    JApplication.getJContext().startActivity(context, intent);
  }
}
