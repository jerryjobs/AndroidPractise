package com.ikaowo.join.modules.mine.service;

import android.content.Context;
import android.content.Intent;

import com.common.framework.core.JApplication;
import com.ikaowo.join.common.service.MineService;
import com.ikaowo.join.modules.mine.activity.MineActivity;
import com.ikaowo.join.modules.mine.activity.UpdatePasswordActivity;

/**
 * Created by weibo on 15-12-29.
 */
public class MineServiceImpl extends MineService {
  @Override
  public void viewUserInfo(Context context) {
    Intent intent = new Intent(context, MineActivity.class);
    JApplication.getJContext().startActivity(context, intent);
  }

  @Override
  public void updatePassword(Context context) {
    Intent intent = new Intent(context, UpdatePasswordActivity.class);
    JApplication.getJContext().startActivity(context, intent);
  }

  @Override
  public void onCreate() {

  }

  @Override
  public void onDestroy() {

  }
}
