package com.ikaowo.join.modules.user.helper;

import android.content.Context;
import android.text.TextWatcher;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by weibo on 15-12-17.
 */
public class InputFiledHelper {

  public EditText getEditText(Context context, int hintRes, TextWatcher textWatcher) {
    EditText editText = new EditText(context);
    editText.setPadding(0, 0, 0, 0);
    editText.setSingleLine();
    editText.setGravity(Gravity.END);
    editText.setTextSize(14);
    editText.setHint(hintRes);
    if (textWatcher != null) {
      editText.addTextChangedListener(textWatcher);
    }
    return editText;
  }

  public TextView getTextView(Context context, int hintRes) {
    TextView tv = new TextView(context);
    tv.setTextColor(tv.getCurrentHintTextColor());

    tv.setHint(hintRes);
    return tv;
  }
}
