package com.ikaowo.join.common.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.common.framework.core.JApplication;
import com.ikaowo.join.R;

/**
 * Created by leiweibo on 6/4/15.
 */
public class NotificationImageView extends ImageView {

  private static int radius = JApplication.getJContext().dip2px(4);
  private static int padding = JApplication.getJContext().dip2px(10);
  private boolean notificationFlag = false;
  private Paint circlePaint;

  public NotificationImageView(Context context) {
    super(context);
    init();
  }

  public NotificationImageView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public NotificationImageView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init();
  }

  private void init() {
    setPadding(padding, padding, padding, padding);
    circlePaint = new Paint();
  }

  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);

    if (notificationFlag) {
      circlePaint.setColor(getResources().getColor(R.color.c11));
      circlePaint.setAntiAlias(true);
      Rect rect = getDrawable().getBounds();

      canvas.drawCircle(getWidth() / 2 + (rect.right - rect.left) / 2,
          getHeight() / 2 - (rect.bottom - rect.top) / 2, radius, circlePaint);
    }
  }

  public void hideNotification() {
    notificationFlag = false;
    postInvalidate();
  }

  public void showNotification() {
    notificationFlag = true;
    postInvalidate();
  }
}
