package com.ikaowo.join.modules.user;

import android.os.Bundle;

import com.ikaowo.join.BaseActivity;
import com.ikaowo.join.R;

/**
 * Created by weibo on 15-12-11.
 */
public class SigninActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

    }

    @Override
    protected String getTag() {
        return "SigninActivity";
    }
}
