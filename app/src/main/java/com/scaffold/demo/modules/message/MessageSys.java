package com.scaffold.demo.modules.message;

import android.content.Context;
import android.view.ViewGroup;

import com.common.framework.activity.BaseSys;
import com.common.framework.core.JFragment;
import com.scaffold.demo.R;

/**
 * Created by weibo on 15-12-8.
 */
public class MessageSys extends BaseSys {

    public MessageSys(Context context, ViewGroup tabContainer, TabListener listener) {
        super(context, tabContainer, listener);
    }

    @Override
    protected JFragment createFragment() {
        return new MessageFragment();
    }

    @Override
    protected int getTabIcon() {
        return R.drawable.home_tab_icon;
    }

    @Override
    protected String getTabTitle() {
        return "消息";
    }

    @Override
    public String getTag() {
        return "MsgSys";
    }

    @Override
    public int getMenu() {
        return R.menu.menu_msg;
    }
}
