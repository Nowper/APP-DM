package com.huanjing.iotapp.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;

import androidx.core.app.NotificationCompat;

import com.huanjing.iotapp.R;

public class NotificationUtils {

    private static final long VIBRATE_DURATION = 1000; // 震动持续时间为1秒

    private Context mContext;

    public NotificationUtils(Context context) {
        mContext = context;
    }

    // 控制手机震动
    public void vibrate() {
        Vibrator vibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null) {
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    // 控制手机响铃
    public void ring() {
        Uri ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone ringtone = RingtoneManager.getRingtone(mContext, ringtoneUri);
        if (ringtone != null) {
            ringtone.play();
        }
    }


    //tuisong推送
    public static void showNotification(Context context, String content) {


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channel_id")
                .setSmallIcon(R.drawable.push) // 设置通知的小图标
                .setContentTitle("提醒") // 设置通知的标题
                .setContentText(content) // 设置通知的内容
                .setAutoCancel(true) // 设置点击通知后自动清除
                .setColor(Color.BLUE); // 设置通知的颜色

        NotificationChannel channel = new NotificationChannel("channel_id", "Channel Name", NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);

        notificationManager.notify(1, builder.build()); // 发送通知，ID为1



        // 发送通知
    }
}