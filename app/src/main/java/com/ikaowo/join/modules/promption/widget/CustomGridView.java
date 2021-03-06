package com.ikaowo.join.modules.promption.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by weibo on 16-1-28.
 */
public class CustomGridView extends GridView {
  public CustomGridView(Context context) {
    super(context);
  }

  public CustomGridView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int heightSpec;

    if (getLayoutParams().height == LayoutParams.WRAP_CONTENT) {

      // The two leftmost bits in the height measure spec have
      // a special meaning, hence we can't use them to describe height.
      heightSpec = MeasureSpec.makeMeasureSpec(
          Integer.MAX_VALUE >>2, MeasureSpec.AT_MOST);
    }
    else {
      // Any other height should be respected as is.
      heightSpec = heightMeasureSpec;
    }

    super.onMeasure(widthMeasureSpec, heightSpec);
  }
}
