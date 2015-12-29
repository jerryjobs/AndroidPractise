package com.ikaowo.join.modules.promption.activity;

import com.common.framework.core.JAdapter;
import com.common.framework.network.NetworkCallback;
import com.common.framework.widget.listview.RecyclerViewHelper;
import com.ikaowo.join.modules.common.BaseListActivity;

/**
 * Created by weibo on 15-12-29.
 */
public class JoinedListActivity extends BaseListActivity {

  @Override
  protected boolean isSupportLoadMore() {
    return false;
  }

  @Override
  protected void sendHttpRequest(NetworkCallback call, int cp, int ps) {

  }

  @Override
  protected void performCustomItemClick(Object o) {

  }

  @Override
  protected JAdapter getAdapter(RecyclerViewHelper recyclerViewHelper) {
    return null;
  }

  @Override
  protected String getEmptyHint() {
    return null;
  }

  @Override
  protected String getTag() {
    return null;
  }
}
