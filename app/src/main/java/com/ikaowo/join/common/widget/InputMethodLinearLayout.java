package com.ikaowo.join.common.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * 继承自LinearLayout， 当键盘或者消失的时候，可以检测到view的size的变化，
 * 然后做对应的滚动操作。
 * Created by weibo on 16-1-20.
 */
public class InputMethodLinearLayout extends LinearLayout {
  protected OnSizeChangedListenner onSizeChangedListenner;
  private int width;
  private boolean sizeChanged = false; //变化的标志
  private int height;
  private int screenWidth; //屏幕宽度
  private int screenHeight; //屏幕高度
  private Scroller scroller;

  public InputMethodLinearLayout(Context paramContext,
      AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    Display localDisplay = ((Activity) paramContext).getWindowManager()
        .getDefaultDisplay();
    this.screenWidth = localDisplay.getWidth();
    this.screenHeight = localDisplay.getHeight();
    init();
  }

  public InputMethodLinearLayout(Context paramContext,
      AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    init();
  }


  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    this.width = widthMeasureSpec;
    this.height = heightMeasureSpec;
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
  }

  @Override
  public void onSizeChanged(int w, int h, int oldw, int oldh) {
    Log.e("weiboooo", "w:" + w + ", h:" + h +", oldw:" + oldw + ", oldh:" + oldh);
    if ((this.onSizeChangedListenner != null)
        && (w == oldw)
        && (oldw != 0)
        && (oldh != 0)) {
      if (h >= oldh) {
        this.sizeChanged = false;
      } else {
        this.sizeChanged = true;
      }

      /*
       * 页面改变>1/4屏幕的时候，才去触动onSizeChange, 某些时候，同一个页面里面type切换，会引起页面的sizechange.
       */
      if (Math.abs(oldh - h) > screenHeight / 4) {
        this.onSizeChangedListenner.onSizeChange(this.sizeChanged, oldh, h);
      }
    }
  }

  @Override
  public void scrollTo(int x, int y) {
    super.scrollTo(x, y);
    postInvalidate();
  }

  @Override
  public void computeScroll() {
    if (!scroller.isFinished()) {
      if (scroller.computeScrollOffset()) {
        int oldX = getScrollX();
        int oldY = getScrollY();
        int x = scroller.getCurrX();
        int y = scroller.getCurrY();
        if (oldX != x || oldY != y) {
          scrollTo(x, y);
        }
        // Keep on drawing until the animation has finished.
        invalidate();
      } else {
        clearChildrenCache();
      }
    } else {
      clearChildrenCache();
    }
  }

  public void smoothScrollTo(int y, int duration) {

    int oldScrollY = getScrollY();
    scroller.startScroll(getScrollX(), oldScrollY, getScrollX(), y, duration);
    invalidate();
  }

  /**
   * 设置视图偏移的监听事件
   *
   * @param paramonSizeChangedListenner
   */
  public void setOnSizeChangedListenner(
      InputMethodLinearLayout.OnSizeChangedListenner paramonSizeChangedListenner) {
    this.onSizeChangedListenner = paramonSizeChangedListenner;
  }

  /**
   * 视图偏移的内部接口
   *
   * @author junjun
   */
  public interface OnSizeChangedListenner {
    void onSizeChange(boolean paramBoolean, int oldh, int h);
  }

  private void enableChildrenCache() {
    final int count = getChildCount();
    for (int i = 0; i < count; i++) {
      final View layout = (View) getChildAt(i);
      layout.setDrawingCacheEnabled(true);
    }
  }

  private void clearChildrenCache() {
    final int count = getChildCount();
    for (int i = 0; i < count; i++) {
      final View layout = (View) getChildAt(i);
      layout.setDrawingCacheEnabled(false);
    }
  }

  private void init() {
    scroller = new Scroller(getContext());
  }


}
