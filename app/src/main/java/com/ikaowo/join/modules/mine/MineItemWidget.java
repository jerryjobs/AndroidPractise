package com.ikaowo.join.modules.mine;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.component.photo.FullImageView;
import com.ikaowo.join.R;

/**
 * Created by weibo on 15-12-29.
 */
public class MineItemWidget extends LinearLayout {

  private Context context;
  private TextView rightTv;
  private FullImageView imageView;
  private TextView shortNameTv;

  public MineItemWidget(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs, 0);
    this.context = context;

    init(attrs);
  }

  private void init(AttributeSet attrs) {
    LayoutInflater layoutInflater = LayoutInflater.from(context);
    RelativeLayout relativeLayout = (RelativeLayout) layoutInflater.inflate(R.layout.widget_mine_item2, null);

    TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MineItemWidget);
    String type = typedArray.getString(R.styleable.MineItemWidget_type);
    Drawable iconDrawable = typedArray.getDrawable(R.styleable.MineItemWidget_item_icon);

    TextView titleTv = (TextView) relativeLayout.findViewById(R.id.desc);
    String title = typedArray.getString(R.styleable.MineItemWidget_item_title);
    titleTv.setText(title);

    int width = typedArray.getDimensionPixelSize(R.styleable.MineItemWidget_icon_height, 0);
    int height = typedArray.getDimensionPixelSize(R.styleable.MineItemWidget_icon_height, 0);

    int iconMargin = typedArray.getDimensionPixelSize(R.styleable.MineItemWidget_icon_margin, 0);

    if ("icon".equals(type)) {
      imageView = new FullImageView(context);
      shortNameTv = new TextView(context);
      shortNameTv.setGravity(Gravity.CENTER);
      shortNameTv.setVisibility(GONE);
      RelativeLayout.LayoutParams rlp;
      if (width > 0 && height > 0) {
        rlp = new RelativeLayout.LayoutParams(width, height);
      } else {
        rlp = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
      }

      rlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
      rlp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
//      if (iconMargin > 0) {
//        rlp.setMargins(iconMargin, iconMargin, 0, iconMargin);
//      }
      relativeLayout.addView(imageView, rlp);
      relativeLayout.addView(shortNameTv, rlp);

      if (iconDrawable != null) {
        if (Build.VERSION.SDK_INT <= 15) {
          imageView.setBackgroundDrawable(iconDrawable);
        } else {
          imageView.setBackground(iconDrawable);
        }
      }
    } else if ("text".equals(type)) {
      rightTv = new TextView(context);
      RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
              ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
      rlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
      rlp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
      relativeLayout.addView(rightTv, rlp);
    } else {
      throw new IllegalArgumentException("type 不正确");
    }

    ViewGroup.LayoutParams vlp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    addView(relativeLayout, vlp);
  }

  public void setText(String text) {
    if (rightTv != null) {
      rightTv.setText(text);
    }
  }

  public void setTextColor(int color) {
    rightTv.setTextColor(ContextCompat.getColor(context, color));
  }

  public FullImageView getImageView() {
    return imageView;
  }

  public TextView getShortNameTv() {
    return shortNameTv;
  }
}
