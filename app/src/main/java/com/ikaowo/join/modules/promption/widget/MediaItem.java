package com.ikaowo.join.modules.promption.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.ikaowo.join.R;

/**
 * Created by weibo on 16-1-8.
 */
public class MediaItem extends LinearLayout {

  private Context context;
  public MediaItem(Context context) {
    super(context);
    this.context = context;
    init();
  }

  public MediaItem(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.context = context;
    init();
  }

  private void init() {
    LayoutInflater layoutInflater = LayoutInflater.from(context);
    layoutInflater.inflate(R.layout.content_complete_promption, this);
  }
}
