package com.example.java_1d;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import java.sql.SQLOutput;
import java.text.SimpleDateFormat;


import androidx.core.app.NotificationCompat;
import java.util.*;

public class NotificationService extends Service {
    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    private final static String default_notification_channel_id = "default";
    Timer timer;
    TimerTask timerTask;
    String TAG = "Timers";
    int Your_X_SECS = 5;
    @Override
    public IBinder onBind (Intent arg0){
        return null;
    }
    @Override
    public int onStartCommand (Intent intent , int flags , int startId) {
        Log. e ( TAG , "onStartCommand" ) ;
        super .onStartCommand(intent , flags , startId) ;
        startTimer() ;
        return START_STICKY ;
    }
    @Override
    public void onCreate () {
        SharedPreferences sharedPreferences = getSharedPreferences("Info", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Log.e( TAG , "onCreate");
        editor.putString("title","Food");
        editor.putString("location","SUTD");
        editor.putString("description","Hello World");
        editor.putString("fromDate","16-11-2019");
        editor.putString("fromTime","07:07");
        editor.putString("toDate","16-11-2019");
        editor.putString("toTime","");
        editor.putBoolean("reminder",false);
        editor.apply();
    }
    @Override
    public void onDestroy () {
        Log. e ( TAG , "onDestroy" ) ;
//        stopTimerTask() ;
        initializeTimerTask();
        super.onDestroy() ;

    }
    //we are going to use a handler to be able to run in our TimerTask
    final Handler handler = new Handler() ;
    public void startTimer () {
        timer = new Timer() ;
        initializeTimerTask() ;
        // Service restarts after being destroyed and runs initisliseTimerTask every min.
        // Sends notifications when fromtime and fromdate is equals to current time and date.
        timer .schedule( timerTask, 60000, Your_X_SECS * 1000) ; //
    }
    public void stopTimerTask () {
        if ( timer != null ) {
            timer.cancel() ;
            timer = null;
        }
    }
    public void initializeTimerTask () {
        timerTask = new TimerTask() {
            public void run () {
                handler.post( new Runnable() {
                    public void run () {
                        createNotification();
                    }
                });
            }
        } ;
    }
    private void createNotification () {
        SharedPreferences sharedPreferences = getSharedPreferences("Info", Context.MODE_PRIVATE);
        String datestored = sharedPreferences.getString("fromDate","");
        String timestored = sharedPreferences.getString("fromTime","");
        SimpleDateFormat dayformatter = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat timeformatter = new SimpleDateFormat("HH:mm");
        Date date = new Date();
        Date date1 = new Date();
        String datenow = dayformatter.format(date);
        String timenow = timeformatter.format(date1);
        System.out.println(timenow);
        System.out.println(timestored);
        if (datestored.equals(datenow) && timestored.equals(timenow)) {
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), default_notification_channel_id);
            mBuilder.setContentTitle("My Notification");
            mBuilder.setContentText(timestored);
            mBuilder.setTicker("Notification Listener Service Example");
            mBuilder.setSmallIcon(R.drawable.ic_launcher_foreground);
            mBuilder.setAutoCancel(true);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
                mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
                assert mNotificationManager != null;
                mNotificationManager.createNotificationChannel(notificationChannel);
            }
            assert mNotificationManager != null;
            mNotificationManager.notify((int) System.currentTimeMillis(), mBuilder.build());
        }
    }
}
