package com.ikaowo.join.modules.push.common;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;

import com.ikaowo.join.R;

/**
 * Created by leiweibo on 5/16/15.
 */
public class NotificationHelper {

    private static int currentNotifId = 0;
    private static long[] varibate = new long[]{100, 100};
    private final int id;
    private final long time;
    private Notification notification;
    private Bitmap btm;
    private Context context;
    private NotificationData data;
    private Intent intent;

    public NotificationHelper() {
        id = ++currentNotifId;
        time = System.currentTimeMillis();
    }

    public void displayNotification(Context context, int notifId, String title, String content,
                                    Intent intent) {
        displayNotification(context, notifId, null, title, content, null, intent);
    }

    public void displayNotification(Context context, String title, String content, Intent intent) {
        displayNotification(context, id, title, content, intent);
    }

    public void displayNotification(Context context, String ticker, String title, String content,
                                    Intent intent) {
        displayNotification(context, id, ticker, title, content, null, intent);
    }

    public void displayNotification(Context context, String ticker, String title, String content,
                                    String icon, Intent intent) {
        displayNotification(context, id, ticker, title, content, icon, intent);
    }

    private void displayNotification(Context context, int notifId, String ticker, String title,
                                     String content, String icon, Intent intent) {
        NotificationData data = new NotificationData();
        data.notifId = notifId;
        data.ticker = ticker;
        data.title = title;
        data.content = content;
        data.icon = icon;
        displayNotification(context, data, intent);
    }

    private void displayNotification(final Context context, final NotificationData data,
                                     Intent intent) {
        this.data = data;
        this.intent = intent;
        this.context = context;

        PendingIntent pIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), intent, 0);

        Notification.Builder builder = new Notification.Builder(context)
                .setContentTitle(data.title)
                .setContentText(data.content)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pIntent)
                .setAutoCancel(true);
        Notification myNotification;
        if (Build.VERSION.SDK_INT <= 15) {
            myNotification = builder.getNotification();
        } else {
            myNotification = builder.build();
        }

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(data.notifId, myNotification);
    }

/*    public static int getPushIcon() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            return R.drawable.push_lollipop;
        } else {
            return R.drawable.push;
        }
    }*/

    public class NotificationData {
        public int notifId;
        public String ticker;
        public String title;
        public String content;
        public String icon;
    }
}
