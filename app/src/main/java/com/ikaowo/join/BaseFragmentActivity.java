package com.ikaowo.join;

import com.common.framework.core.JFragmentActivity;
import com.umeng.analytics.MobclickAgent;

/**
 * 含有Fragment的Activity继承这个，跟BaseActivity区别是
 * onResume和onPause里面的区别不一样。
 * Created by weibo on 15-12-1.
 */
public abstract class BaseFragmentActivity extends JFragmentActivity {

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
