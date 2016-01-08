package com.ikaowo.join.eventbus;

/**
 * Created by weibo on 16-1-4.
 */
public interface UpdatePromptionCallback {
  boolean promptionUpdated();

  String getNewTitle();

  String getNewEndTime();
}
