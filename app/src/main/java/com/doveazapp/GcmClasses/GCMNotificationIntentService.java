package com.doveazapp.GcmClasses;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.doveazapp.Activities.AgentAcceptDeclineActivity;
import com.doveazapp.Activities.MenuActivity;
import com.doveazapp.Constants;
import com.doveazapp.R;
import com.doveazapp.Utils.Config;
import com.doveazapp.Utils.SessionManager;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.util.HashMap;

public class GCMNotificationIntentService extends IntentService {

    public static final int NOTIFICATION_ID = 1;

    private NotificationManager mNotificationManager;

    NotificationCompat.Builder builder;

    public GCMNotificationIntentService() {
        super("GcmIntentService");
    }

    public static final String TAG = "GCMNotificationIntentService";

    //session manager
    SessionManager session;

    @SuppressLint("LongLogTag")
    @Override
    protected void onHandleIntent(Intent intent) {
        // Session Manager
        session = new SessionManager(getApplicationContext());
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        // token
        String name = user.get(SessionManager.KEY_APITOKEN);
        // email
        String email = user.get(SessionManager.KEY_EMAIL);

        Bundle extras = intent.getExtras();
        String msg = intent.getStringExtra("message");
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
                    .equals(messageType)) {
                sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
                    .equals(messageType)) {
                sendNotification("Deleted messages on server: "
                        + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
                    .equals(messageType)) {

                for (int i = 0; i < 3; i++) {
                    Log.i(TAG,
                            "Working... " + (i + 1) + "/5 @ "
                                    + SystemClock.elapsedRealtime());
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                    }
                }
                Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());

                sendNotification("Message: " + extras.get(Config.MESSAGE_KEY));
                Log.i(TAG, "Received: " + extras.toString());

            }
        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    @SuppressLint("LongLogTag")
    private void sendNotification(String msg) {
        Log.d(TAG, "Preparing to send notification...: " + msg);
        mNotificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);

        HashMap<String, String> user = session.getUserDetails();
        final String partner = user.get(SessionManager.KEY_USER_TYPE);
        PendingIntent contentIntent = null;
        if (partner.equals(Constants.KEY_TYPE_PARTNER)) {
            contentIntent = PendingIntent.getActivity(this, 0,
                    new Intent(this, AgentAcceptDeclineActivity.class), 0);

        } else if (partner.equals(Constants.KEY_TYPE_DELIVER)) {
            contentIntent = PendingIntent.getActivity(this, 0,
                    new Intent(this, MenuActivity.class), 0);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this);
        //mBuilder.setSmallIcon(getNotificationIcon());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBuilder.setColor(getResources().getColor(R.color.doveaz_orange));
            mBuilder.setSmallIcon(R.drawable.notify_lollipop);

        } else {
            mBuilder.setSmallIcon(R.drawable.notify_icon);
        }
        mBuilder.setContentTitle("Notification from Doveaz!");
        mBuilder.setDefaults(Notification.DEFAULT_ALL);
        mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(msg));
        mBuilder.setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        Log.d(TAG, "Notification sent successfully.");
    }

   /* private int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.drawable.notify_lollipop : R.drawable.notify_icon;

    }*/
}
