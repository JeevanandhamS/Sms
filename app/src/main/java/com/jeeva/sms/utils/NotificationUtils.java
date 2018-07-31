package com.jeeva.sms.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.jeeva.sms.R;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Hetermis on 8/25/2017.
 */

public class NotificationUtils {

    public static String getDefaultNotificationChannelId(Context context, String channel_id, int importance) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(
                    NOTIFICATION_SERVICE);

            if (null != notificationManager) {
                if (null != notificationManager.getNotificationChannel(channel_id)) {
                    return channel_id;
                } else {
                    // Create the NotificationChannel
                    CharSequence name = "Sms Notification";
                    String description = "Sms Notification";
                    NotificationChannel mChannel = new NotificationChannel(channel_id, name, importance);
                    mChannel.setDescription(description);
                    notificationManager.createNotificationChannel(mChannel);
                    return mChannel.getId();
                }
            }
        }

        return channel_id;
    }

    public static boolean presentNotification(Context context,
                                              int notificationId,
                                              String title,
                                              String contentText,
                                              PendingIntent pendingIntent,
                                              int largeIconRes,
                                              Bitmap bigImageBitmap,
                                              NotificationCompat.Action... actions) {

        String channelId = NotificationUtils.getDefaultNotificationChannelId(context,
                "SMS_NOTIFICATION", NotificationManager.IMPORTANCE_LOW);
        if (null == channelId) {
            return false;
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId);
        builder.setAutoCancel(true);
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setWhen(System.currentTimeMillis());
        builder.setContentTitle(title);

        try {
            if(!TextUtils.isEmpty(contentText)) {
                builder.setContentText(contentText);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        builder.setSmallIcon(getNotificationSmallIcon());

        try {
            if (largeIconRes == 0 || largeIconRes == -1) {
                largeIconRes = R.mipmap.ic_launcher;
            }

            builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), largeIconRes));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (null != bigImageBitmap) {
            builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bigImageBitmap));
        }

        builder.setTicker(title);
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        builder.setContentIntent(pendingIntent);

        if (null != actions && actions.length > 0) {
            for (NotificationCompat.Action singeAction : actions) {
                builder.addAction(singeAction);
            }
        }

        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId, builder.build());

        return true;
    }

    public static int getNotificationSmallIcon() {
        if (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            return R.mipmap.ic_launcher;
        } else {
            return R.drawable.ic_stat_message;
        }
    }

    public static void removeNotification(Context context, int notificationId) {
        if (notificationId != 0) {
            NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            if (manager != null) {
                manager.cancel(notificationId);
            }
        }
    }

}