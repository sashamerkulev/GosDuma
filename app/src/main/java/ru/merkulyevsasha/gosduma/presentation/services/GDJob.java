package ru.merkulyevsasha.gosduma.presentation.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;
import ru.merkulyevsasha.gosduma.GosDumaApp;
import ru.merkulyevsasha.gosduma.presentation.main.MainActivity;
import ru.merkulyevsasha.gosduma.R;
import ru.merkulyevsasha.gosduma.domain.NewsServiceInteractor;
import ru.merkulyevsasha.gosduma.models.News;
import ru.merkulyevsasha.gosduma.presentation.KeysBundleHolder;
import ru.merkulyevsasha.gosduma.presentation.news.NewsActivity;


/**
 * Created by sasha_merkulev on 10.07.2017.
 */

public class GDJob extends Job {

    private static final String TAG = GDJob.class.getName();

    private static int lastJobId;

    @Inject NewsServiceInteractor inter;

    GDJob(){
        GosDumaApp.getComponent().inject(this);
    }

    @NonNull
    @Override
    protected Result onRunJob(Params params) {
        inter.getNotificationNews2().subscribe(new Consumer<News>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull News news) throws Exception {
                sendNotification(getContext(), news.getNavId(), getContext().getString(news.getTitleId()), news.getName());
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {

            }
        });
        return Result.SUCCESS;
    }

    public static void scheduleJob() {
        lastJobId = new JobRequest.Builder(GDJob.TAG)
                .setPeriodic(TimeUnit.MINUTES.toMillis(60))
                .setRequiresCharging(false)
                .setRequiresDeviceIdle(false)
                .setRequiredNetworkType(JobRequest.NetworkType.ANY)
                .setPersisted(true)
                .build()
                .schedule();
    }

    public static void sendNotification(Context context, int id, String name, String news){
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
        resultIntent.putExtra(KeysBundleHolder.KEY_NOTIFICATION, true);

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
        mNotificationManager.notify(id, mBuilder.build());
    }


}
