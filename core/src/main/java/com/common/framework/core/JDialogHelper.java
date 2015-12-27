package com.common.framework.core;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by weibo on 15-12-3.
 */
public class JDialogHelper {
  private Activity activity;
  private ProgressDialog dialog;

  public JDialogHelper(Activity activity) {
    this.activity = activity;
  }

  public void showProgressDialog(String msg) {
    showProgressDialog(msg, true, null);
  }

  public void showProgressDialog(String msg, boolean cancelable) {
    showProgressDialog(msg, cancelable, null);
  }

  public void showProgressDialog(final String msg, final boolean cancelable,
                                 final DialogInterface.OnCancelListener cancelListener) {
    dismissProgressDialog();
    activity.runOnUiThread(new Runnable() {

      @Override
      public void run() {
        if (activity == null || activity.isFinishing()) {
          return;
        }

        dialog = ProgressDialog.show(activity, "", msg, true, cancelable);
        if (cancelListener != null) {
          dialog.setOnCancelListener(cancelListener);
        }
      }
    });
  }

  public void dismissProgressDialog() {
    activity.runOnUiThread(new Runnable() {
      @Override
      public void run() {
        if (dialog != null && dialog.isShowing()
          && !activity.isFinishing()) {
          dialog.dismiss();
          dialog = null;
        }
      }
    });
  }

  public Dialog createDialog(Context context, String title, String content,
                             String[] buttons, View.OnClickListener[] listeners) {
    AlertDialog.Builder ab = getDialogBuilder(context, title, content);
    return getDialog(ab, buttons, listeners);
  }

  public Dialog createDialog(Context context
    , String title, View contentView, String[] buttons,
                             View.OnClickListener[] listeners) {
    TextView titleView = getTitleView(context, title);
    AlertDialog.Builder ab = getDialogBuilder(context, titleView, contentView);

    return getDialog(ab, buttons, listeners);
  }

  private AlertDialog.Builder getDialogBuilder(Context context, String title, String content) {
    AlertDialog.Builder builder = new AlertDialog.Builder(context);
    builder.setTitle(title).setMessage(content);
    builder.setCancelable(true);
    return builder;
  }

  private AlertDialog.Builder getDialogBuilder(Context context, TextView titleView, View contentView) {
    AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
    int top = (int) context.getResources().getDimension(R.dimen.abc_dialog_padding_top_material);
    builder.setCustomTitle(titleView).setView(contentView, top, top, top, top);
    builder.setCancelable(true);
    return builder;
  }

  private void setupDialogBuilder(AlertDialog.Builder builder, String[] buttons,
                                  View.OnClickListener[] listeners) {

    if (buttons.length != listeners.length) {
      throw new IllegalArgumentException("buttons's size is not equals listeners's size");
    }
    if (buttons.length != 0 && listeners.length == buttons.length) {
      switch (buttons.length) {
        case 1:
          //确定
          builder.setPositiveButton(buttons[0], null);
          break;
        case 2:
          //取消
          builder.setNegativeButton(buttons[0], null);
          //确定
          builder.setPositiveButton(buttons[1], null);
          break;
        case 3:
          //取消
          builder.setNegativeButton(buttons[0], null);
          //中间
          builder.setNeutralButton(buttons[1], null);
          //确定
          builder.setPositiveButton(buttons[2], null);
          break;
      }
    }

    builder.setOnKeyListener(new DialogInterface.OnKeyListener() {

      @Override
      public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        return keyCode == KeyEvent.KEYCODE_BACK;
      }
    });
  }

  private Dialog getDialog(AlertDialog.Builder ab,
                           final String[] buttons, final View.OnClickListener[] listeners) {
    boolean exception = false;
    try {
      setupDialogBuilder(ab, buttons, listeners);
    } catch (Exception e) {
      exception = true;
    } finally {
      final AlertDialog dialog = ab.create();

      dialog.setOnShowListener(new DialogInterface.OnShowListener() {
        @Override
        public void onShow(DialogInterface dialogInterface) {
          Button positiveBtn = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
          Button negativeBtn = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
          Button neutralBtn = dialog.getButton(AlertDialog.BUTTON_NEUTRAL);
          switch (listeners.length) {
            case 1:
              bindOnClickListener(positiveBtn, listeners[0]);
              break;
            case 2:
              bindOnClickListener(negativeBtn, listeners[0]);
              bindOnClickListener(positiveBtn, listeners[1]);
              break;
            case 3:
              bindOnClickListener(negativeBtn, listeners[0]);
              bindOnClickListener(neutralBtn, listeners[1]);
              bindOnClickListener(positiveBtn, listeners[2]);
              break;
          }
        }
      });

      dialog.setCanceledOnTouchOutside(exception);
      return dialog;
    }
  }

  private void bindOnClickListener(Button button, View.OnClickListener listener) {
    if (button == null) {
      return;
    }
    button.setOnClickListener(listener);

  }

  private TextView getTitleView(Context context, String title) {
    TextView titleView = new TextView(context);
    float top = context.getResources().getDimension(R.dimen.abc_dialog_padding_top_material);
    titleView.setTextColor(context.getResources().getColor(R.color.abc_primary_text_material_light));
    titleView.setText(title);
    titleView.setPadding((int) (top), (int) top, (int) top, 0);

    return titleView;
  }

}
