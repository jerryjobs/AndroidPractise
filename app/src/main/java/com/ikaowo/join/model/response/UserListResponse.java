package com.ikaowo.join.model.response;

import com.ikaowo.join.model.User;
import com.ikaowo.join.model.base.BaseListResponse;

import java.util.List;

/**
 * Created by weibo on 15-12-28.
 */
public class UserListResponse extends BaseListResponse {
  public List<User> data;
}
