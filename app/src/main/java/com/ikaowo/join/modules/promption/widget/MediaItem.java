package com.ikaowo.join.modules.promption.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.ikaowo.join.R;

/**
 * Created by weibo on 16-1-8.
 */
public class MediaItem extends LinearLayout {

  private Context context;

  private AppCompatEditText editText;
  private TextView titleTv;

  public MediaItem(Context context) {
    super(context);
    this.context = context;
    init();
  }

  public MediaItem(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.context = context;

    init();

    TypedArray typedValue = context.obtainStyledAttributes(attrs, R.styleable.MediaItem);
    String title = typedValue.getString(R.styleable.MediaItem_media_title);
    titleTv.setText(title);
    typedValue.recycle();
  }

  private void init() {
    LayoutInflater layoutInflater = LayoutInflater.from(context);
    layoutInflater.inflate(R.layout.widget_media_item, this);
    editText = (AppCompatEditText) findViewById(R.id.media_url);
    editText.setSingleLine(true);
    titleTv = (TextView) findViewById(R.id.title);
  }

  public void setTitle(String title) {
    titleTv.setText(title);
  }

  public String getValue() {
    return editText.getText().toString().trim();
  }
}
