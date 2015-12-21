package com.ikaowo.join.util;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;


/**
 * Created by weibo on 15-12-11.
 */
public class ResourceUtil {
  public static int getColor(Context context, int colorRes) {
    int color;
    if (Build.VERSION.SDK_INT >= 23) {
      color = context.getResources().getColor(colorRes, context.getTheme());
    } else {
      color = context.getResources().getColor(colorRes);
    }
    return color;
  }

  public static Drawable getDrawable(Context context, int drawableRes) {
    Drawable drawable = null;
    if (Build.VERSION.SDK_INT >= 21) {
      drawable = context.getResources()
              .getDrawable(drawableRes, context.getTheme());
    } else {
      drawable = context.getResources()
              .getDrawable(drawableRes);
    }

    return drawable;
  }

  public static Drawable updateDrawableColor(Context context, int srcDrawable, int color) {
    final Drawable upArrow = ContextCompat.getDrawable(context, srcDrawable);
    upArrow.setColorFilter(ContextCompat.getColor(context, color), PorterDuff.Mode.SRC_ATOP);
    return upArrow;
  }
}
