package com.common.framework.core;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by weibo on 15-12-1.
 */
@Retention(RetentionPolicy.RUNTIME) public @interface JAction {
  Class<?> ai();
}