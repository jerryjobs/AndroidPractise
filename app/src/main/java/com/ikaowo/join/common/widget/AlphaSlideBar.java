package com.ikaowo.join.common.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.ikaowo.join.R;

/**
 * Created by weibo on 15-12-17.
 */
public class AlphaSlideBar extends View {
  private String[] alphas = {"A", "B", "C", "D", "E", "F", "G", "H", "I",
          "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
          "W", "X", "Y", "Z", "#"};

  private Paint paint = new Paint();
  private int choosedPos = -1;
  private OnTouchingLetterChangedListener listener;

  public AlphaSlideBar(Context context) {
    super(context);
  }

  public AlphaSlideBar(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public AlphaSlideBar(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);

    int width = getWidth();
    int height = getHeight();
    int singleHeight = height / alphas.length;

    for (int i = 0; i < alphas.length; i++) {
      paint.setColor(ContextCompat.getColor(getContext(), R.color.c4));
      // paint.setColor(Color.WHITE);
      paint.setAntiAlias(true);
      paint.setTextSize(30);
      // 选中的状态
      if (i == choosedPos) {
        paint.setColor(ContextCompat.getColor(getContext(), R.color.c4));
        paint.setFakeBoldText(true);
      }
      // x坐标等于中间-字符串宽度的一半.
      float xPos = width / 2 - paint.measureText(alphas[i]) / 2;
      float yPos = singleHeight * i + singleHeight;
      canvas.drawText(alphas[i], xPos, yPos, paint);
      paint.reset();
    }
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    final int action = event.getAction();
    final float y = event.getY();
    final int oldChoosedPos = choosedPos;

    int pos = (int) (y / getHeight() * alphas.length);

    switch (action) {
      case MotionEvent.ACTION_UP:
        setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.transparent));
        setAlpha(1);
        choosedPos = -1;
        invalidate();
        if (listener != null) {
          listener.onTouchUp();
        }
        break;

      default:
        setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.black));
        setAlpha(0.4f);
        if (oldChoosedPos != pos) {
          if (pos >= 0 && pos < alphas.length) {
            if (listener != null) {
              listener.onTouchingLetterChanged(alphas[pos]);
            }
            choosedPos = pos;
            invalidate();
          }
        }
    }

    return true;
  }

  public void setOnTouchingLetterChangedListener(OnTouchingLetterChangedListener listener) {
    this.listener = listener;
  }

  public interface OnTouchingLetterChangedListener {
    void onTouchingLetterChanged(String s);

    void onTouchUp();
  }
}
