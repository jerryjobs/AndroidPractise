package com.ikaowo.join.modules.message;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.ViewGroup;
import com.common.framework.activity.BaseSys;
import com.common.framework.core.JApplication;
import com.common.framework.interceptor.JInterceptor;
import com.ikaowo.join.R;
import com.ikaowo.join.common.service.UserService;
import com.ikaowo.join.im.helper.LoginHelper;

/**
 * Created by weibo on 15-12-8.
 */
public class MessageSys extends BaseSys {

  public MessageSys(Context context, ViewGroup tabContainer, TabListener listener) {
    super(context, tabContainer, listener);
  }

  @Override protected Fragment createFragment() {
    LoginHelper loginHelper = LoginHelper.getInstance();
    return loginHelper.getIMKit().getConversationFragment();
  }

  protected JInterceptor.Stub createInterceptor() {
    return new JInterceptor.Stub() {
      public boolean check() {
        UserService userService =
            JApplication.getJContext().getServiceByInterface(UserService.class);
        userService.interceptorCheckUserState(context, R.string.action_msg,
            new UserService.AuthedAction() {
              @Override public void doActionAfterAuthed() {
                performClick();
              }
            });
        return false;
      }
    };
  }

  public Fragment getFragment() {
    if (reseted) {
      reseted = false;
      fragment = createFragment();
    }
    return super.getFragment();
  }

  @Override protected int getTabIcon() {
    return R.drawable.message_tab_icon;
  }

  @Override protected String getTabTitle() {
    return context.getString(R.string.title_message);
  }

  @Override protected String getActionBarTitle() {
    return context.getResources().getString(R.string.title_message);
  }

  @Override public String getTag() {
    return "MsgSys";
  }

  @Override public int getMenu() {
    return 0;
  }
}
