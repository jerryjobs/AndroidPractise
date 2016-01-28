package com.ikaowo.join.eventbus;

import com.ikaowo.join.model.UserLatestState;

/**
 * Created by weibo on 16-1-6.
 */
public interface CheckLatestStateCallback {
  UserLatestState getLatestState();
}
