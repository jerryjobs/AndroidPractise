package com.ikaowo.marketing;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.common.framework.core.JFragment;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by weibo on 15-12-8.
 */
public abstract class BaseFragment extends JFragment {

    public abstract String getPageName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getPageName());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getPageName());
    }
}
