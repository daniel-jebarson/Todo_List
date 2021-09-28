package com.example.todolist;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Random;

public class RemainderBroadcast extends BroadcastReceiver {
//        private static int x=200;
    private static String title,work;
    @Override
    public void onReceive(Context context, Intent intent) {


//        System.out.println(x);
//        NotificationCompat.Builder notification=new NotificationCompat.Builder(context,"notify")
//                .setSmallIcon(R.drawable.ic_baseline_add_alert_24)
//                .setContentTitle("hi there")
//                .setContentText("Think this is the time you asked us to remaind")
//                .setPriority(NotificationCompat.PRIORITY_HIGH);
        title=intent.getStringExtra("title");
        work=intent.getStringExtra("work");
        NotificationCompat.Builder notification=new NotificationCompat.Builder(context,"notify")
                .setSmallIcon(R.drawable.ic_baseline_add_alert_24)
                .setContentTitle(title)
                .setContentText(work)
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        NotificationManagerCompat notificationManagerCompat=NotificationManagerCompat.from(context);
//        SystemClock.sleep(1000);
        notificationManagerCompat.notify(200,notification.build());
//        x=x+1;


    }
}
