package com.ikaowo.join;

import android.os.Bundle;

import de.greenrobot.event.EventBus;

/**
 * Created by weibo on 15-12-21.
 */
public abstract class BaseEventBusActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
