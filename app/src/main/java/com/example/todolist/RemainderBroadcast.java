package com.example.todolist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class RemainderBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder builder=new NotificationCompat.Builder(context,"notifyid")
                .setSmallIcon(R.drawable.ic_baseline_add_alert_24)
                .setContentTitle("Remainder for task")
                .setContentText("This is the remainder time you set on the task")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManagerCompat=NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(200,builder.build());
        //for second notification
        // notificationManagerCompat.notify(201,builder.build());

    }
}
