package com.ikaowo.join.modules.promption.activity;

import android.os.Bundle;

import com.ikaowo.join.BaseFragmentActivity;
import com.ikaowo.join.R;

/**
 * Created by weibo on 15-12-29.
 */
public class JoinActivity extends BaseFragmentActivity {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_join);
  }

  @Override
  protected String getTag() {
    return "JoinActivity";
  }
}
