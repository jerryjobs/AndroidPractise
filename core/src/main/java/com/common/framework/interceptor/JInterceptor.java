package com.common.framework.interceptor;

import android.content.Context;
import android.content.Intent;

/**
 * Created by weibo on 15-12-1.
 */
public interface JInterceptor {

    boolean check(Context context, Intent intent);

    boolean check();

    class Stub implements JInterceptor {
        @Override
        public boolean check() {
            return false;
        }

        @Override
        public boolean check(Context context, Intent intent) {
            return false;
        }
    }
}