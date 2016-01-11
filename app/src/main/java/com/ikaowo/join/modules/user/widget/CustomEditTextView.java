package com.ikaowo.join.modules.user.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.ikaowo.join.R;

/**
 * Created by weibo on 15-12-16.
 */
public class CustomEditTextView extends LinearLayout {
  @Bind(R.id.right_container) LinearLayout containerLayout;
  @Bind(R.id.title) TextView titleView;
  @Bind(R.id.splite_view) View splitView;

  private Context context;

  public CustomEditTextView(Context context) {
    super(context);
    this.context = context;
    init();
  }

  public CustomEditTextView(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.context = context;
    init();

    TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomEditTextView);
    boolean showSplit = typedArray.getBoolean(R.styleable.CustomEditTextView_show_split, true);
    splitView.setVisibility(showSplit ? VISIBLE : INVISIBLE);

    int color = typedArray.getColor(R.styleable.CustomEditTextView_title_text_color,
        ContextCompat.getColor(context, R.color.c3));
    titleView.setTextColor(color);

    String title = typedArray.getString(R.styleable.CustomEditTextView_title_text);
    titleView.setText(title);
  }

  private void init() {
    LayoutInflater.from(context).inflate(R.layout.widget_custom_edittext, this);
    ButterKnife.bind(this, this);
  }

  public void setTitle(String title) {
    titleView.setText(title);
  }

  public void addRightView(View view, int color) {
    if (view == null) {
      return;
    }

    view.setBackgroundResource(0);
    if (color > 0 && view instanceof TextView) {
      ((TextView) view).setTextColor(ContextCompat.getColor(context, color));
    }

    LinearLayout.LayoutParams llp;
    if (view instanceof EditText) {
      llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
          ViewGroup.LayoutParams.WRAP_CONTENT);
      llp.weight = 1;
    } else {
      llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
          ViewGroup.LayoutParams.WRAP_CONTENT);
    }
    llp.gravity = Gravity.CENTER_VERTICAL;
    containerLayout.addView(view, llp);
  }
}
