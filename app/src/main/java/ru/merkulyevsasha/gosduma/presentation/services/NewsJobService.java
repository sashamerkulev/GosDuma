package ru.merkulyevsasha.gosduma.presentation.services;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;

import java.util.concurrent.ExecutorService;

import javax.inject.Inject;

import ru.merkulyevsasha.gosduma.GosDumaApp;


@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class NewsJobService extends JobService {

    @Inject
    ExecutorService executor;

    @Override
    public boolean onStartJob(JobParameters params) {

        GosDumaApp.getComponent().inject(this);

        System.out.println("NewsJobService: start job");

        if (!ServicesHelper.isBatteryGood(this)){
            System.out.println("NewsJobService: stop service: battery low");
            return false;
        }

        System.out.println("NewsJobService: executor submit");
        executor.submit(new NewsRunnable(this.getApplicationContext()));

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {

        System.out.println("NewsJobService: stop job");

        return true;
    }
}
