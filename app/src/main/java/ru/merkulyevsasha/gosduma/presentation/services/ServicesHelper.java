package ru.merkulyevsasha.gosduma.presentation.services;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import java.util.Calendar;

import ru.merkulyevsasha.gosduma.MainActivity;
import ru.merkulyevsasha.gosduma.R;
import ru.merkulyevsasha.gosduma.presentation.KeysBundleHolder;
import ru.merkulyevsasha.gosduma.presentation.news.NewsActivity;


public class ServicesHelper {

    static String TAG = "ServicesHelper";

    public static final String ALARM_ACTION_NAME = "ru.merkulyevsasha.easytodo.START_SERVICE";

    private static final int ALARM_AFTER_MINUTES = 1;
    private static final int NOTIFICATION_ID = 989;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void registerJobService(Context context){
        System.out.println("registering job!!");
        Log.d(TAG, "registering job!!");
        ComponentName serviceName = new ComponentName(context, NewsJobService.class);
        JobInfo jobInfo = new JobInfo.Builder(1, serviceName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setRequiresDeviceIdle(true)
                .setPeriodic(ServicesHelper.ALARM_AFTER_MINUTES*60*1000)
                .setRequiresCharging(true)
                .build();

        JobScheduler scheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        int result = scheduler.schedule(jobInfo);
        if (result == JobScheduler.RESULT_SUCCESS){
            Log.d(TAG, "Good job, son!!");
            System.out.println("Good job, son!!");
        }
    }

    public static void registerAlarmNewsService(Context context){
        System.out.println("registering alarm!!");
        Log.d(TAG, "registering alarm!!");
        AlarmManager alarmManager= (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, Reciever.class);
        intent.setAction(ServicesHelper.ALARM_ACTION_NAME);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, ServicesHelper.ALARM_AFTER_MINUTES);

        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);

    }

    public static void register(Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ServicesHelper.registerJobService(context);
        } else {
            ServicesHelper.registerAlarmNewsService(context);
        }
    }

    public static void setNotification(Context context, int id, String name, String news){
        System.out.println("registering notification!!");
        Log.d(TAG, "registering notification!!");
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setContentTitle(context.getString(R.string.app_name))
                        .setContentText(news)
                        .setAutoCancel(true);
// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(context, NewsActivity.class);
        resultIntent.putExtra(KeysBundleHolder.KEY_ID, id);
        resultIntent.putExtra(KeysBundleHolder.KEY_NAME, name);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(ServicesHelper.NOTIFICATION_ID, mBuilder.build());
    }


}
