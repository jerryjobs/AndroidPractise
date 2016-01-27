package com.ikaowo.join.model.request;

import com.ikaowo.join.model.base.BaseRequest;

/**
 * Created by weibo on 15-12-29.
 */
public class UpdatePasswordRequest extends BaseRequest {
    public String new_password;
    public String old_password;
}
