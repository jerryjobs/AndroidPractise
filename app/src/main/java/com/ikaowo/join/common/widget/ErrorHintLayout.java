package com.ikaowo.join.common.widget;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.framework.core.JApplication;
import com.ikaowo.join.R;
import com.ikaowo.join.common.AnimatorWrapper;

/**
 * Created by leiweibo on 1/9/16.
 */
public class ErrorHintLayout extends LinearLayout {

  private Context context;
  private TextView textView;
  public ErrorHintLayout(Context context) {
    super(context);
    this.context = context;
    init();
  }

  public ErrorHintLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.context = context;
    init();
  }

  private void init() {
    LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
      JApplication.getJContext().dip2px(30));
    LinearLayout errHintLayout = new LinearLayout(context);
    errHintLayout.setOrientation(LinearLayout.VERTICAL);
    errHintLayout.setLayoutParams(llp);
    errHintLayout.setGravity(Gravity.CENTER_VERTICAL);
    textView = new TextView(context);
    textView.setPadding(JApplication.getJContext().dip2px(20), 0, 0, 0);
    textView.setBackgroundColor(ContextCompat.getColor(context, R.color.c12));
    textView.setTextColor(ContextCompat.getColor(context, R.color.c11));
    textView.setGravity(Gravity.CENTER_VERTICAL);
    textView.setLayoutParams(llp);
    textView.setSingleLine();
    textView.setEllipsize(TextUtils.TruncateAt.END);
    errHintLayout.addView(textView);
    View view = new View(context);
    view.setBackgroundColor(ContextCompat.getColor(context, R.color.c13));
    llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, JApplication.getJContext().dip2px(1));
    view.setLayoutParams(llp);
    errHintLayout.addView(view);

    addView(errHintLayout);

    new AnimatorWrapper().startTranslationAnimation(this, 2000);
  }

  public void setText(String text) {
    textView.setText(text);

  }
}
