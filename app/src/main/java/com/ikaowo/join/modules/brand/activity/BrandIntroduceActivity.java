package com.ikaowo.join.modules.brand.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.ikaowo.join.BaseActivity;
import com.ikaowo.join.R;
import com.ikaowo.join.util.Constant;

/**
 * Created by weibo on 16-1-18.
 */
public class BrandIntroduceActivity extends BaseActivity {

  @Bind(R.id.brand_introduce) TextView introduceTv;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_brand_introduce);
    ButterKnife.bind(this);

    toolbar = (Toolbar) findViewById(R.id.toolbar);
    toolbar.setTitle("品牌介绍");
    setSupportActionBar(toolbar);

    ActionBar ab = getSupportActionBar();
    ab.setDisplayHomeAsUpEnabled(true);

    String content = getIntent().getStringExtra(Constant.INTRODUCE_INTENT_EXTRA);
    if (!TextUtils.isEmpty(content)) {
      introduceTv.setText(content);
    } else {
      introduceTv.setText(R.string.empty_hint_no_brand_introduce);
    }
  }

  @Override protected String getTag() {
    return "BrandIntroduceActivity";
  }
}
