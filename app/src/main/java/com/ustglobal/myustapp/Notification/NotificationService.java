package com.ustglobal.myustapp.Notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.ustglobal.myustapp.Dashboard.SubCategoryList;
import com.ustglobal.myustapp.R;

/**
 * Created by Shubham Singhal.
 */

public class NotificationService extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";
    private String type,message;
    private Handler handler = new Handler();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO: Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated.
        String str = remoteMessage.getNotification().getBody();
        String[] split = str.split("@");
        message = split[0].trim();
        type = split[1].trim();

        if (remoteMessage.getData().size() > 0) {
            //create notification
            String str1 = remoteMessage.getNotification().getBody();
            String[] split1 = str1.split("@");
            message = split1[0].trim();
            type = split1[1].trim();
        }


        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
        generateNotification(getApplicationContext(),message);
    }


    private void generateNotification(Context context,String message) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationIntent = new Intent(context, SubCategoryList.class);
        notificationIntent.putExtra("obj", type);
//       Constants.notification = true;
        Log.e("CLICK",type);
        Log.e("MESSAGE",message);

       // notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.app_logo)
                .setContentTitle("My UST")
                .setContentText(message)
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(message))
                .build();

        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        // Play default notification sound
        notification.defaults |= Notification.DEFAULT_SOUND;
        // Vibrate if vibrate is enabled
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notification.defaults |= Notification.DEFAULT_ALL;
        notificationManager.notify(999999, notification);

    }


}
//Bundle[{google.c.a.c_l=test test test test test, google.c.a.udt=0, google.sent_time=1499141144010, gcm.notification.e=1, google.c.a.c_id=2275094094262417160, google.c.a.ts=1499141144, gcm.n.e=1, from=849191857386, google.message_id=0:1499141144329698%cfc3c855cfc3c855, gcm.notification.body=test, google.c.a.e=1, collapse_key=com.internal.ustglobal}]