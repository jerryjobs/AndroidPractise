package com.common.framework.model;

import java.io.Serializable;

/**
 * Created by weibo on 15-12-21.
 */
public class JErrorReponse implements Serializable {
    public int status;
    public String msg;
    public Object err;
}
