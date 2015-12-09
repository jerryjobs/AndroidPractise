package com.common.framework.util;

import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.common.framework.core.JApplication;
import com.common.framework.core.R;

/**
 * Created by weibo on 15-12-2.
 */
public class JToast {
    private static Handler handler = new Handler(Looper.getMainLooper());
    private static Toast toast;
    private static void toast(final String str, final int time) {
        handler.post(new Runnable() {

            @Override
            public void run() {
                if (toast == null) {
                    toast = Toast.makeText(JApplication.getInstance().getApplicationContext(), str, time);
                }
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                TextView view = new TextView(JApplication.getInstance().getApplicationContext());
                int padding = JApplication.getInstance().getResources().getDimensionPixelOffset(R.dimen.toast_padding);
                view.setPadding(padding, padding, padding, padding);
                view.setBackgroundResource(R.drawable.x_toast_back);
                view.setText(str);
                view.setGravity(Gravity.CENTER);
                view.setTextColor(Color.WHITE);
                toast.setView(view);
                toast.show();
            }
        });
    }

    public static void toastShort(String str) {
        toast(str, Toast.LENGTH_SHORT);
    }

    public static void toastLong(String str) {
        toast(str, Toast.LENGTH_LONG);
    }
}
