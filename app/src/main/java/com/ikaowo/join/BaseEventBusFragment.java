package com.ikaowo.join;

import android.os.Bundle;
import android.support.annotation.Nullable;

import de.greenrobot.event.EventBus;

/**
 * Created by weibo on 15-12-30.
 */
public abstract class BaseEventBusFragment extends BaseFragment {

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    EventBus.getDefault().register(this);
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    EventBus.getDefault().unregister(this);
  }
}
