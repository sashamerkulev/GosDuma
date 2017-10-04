package ru.merkulyevsasha.gosduma;

import android.app.Activity;
import android.app.Application;
import android.app.Service;

import com.evernote.android.job.JobManager;
import com.google.android.gms.ads.MobileAds;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import ru.merkulyevsasha.gosduma.dagger.components.AppComponent;
import ru.merkulyevsasha.gosduma.dagger.components.DaggerAppComponent;


public class GosDumaApp extends Application implements HasActivityInjector {

    @Inject DispatchingAndroidInjector<Activity> activityInjector;
    @Inject DispatchingAndroidInjector<Service> serviceInjector;

    @Override
    public void onCreate() {
        super.onCreate();

        AppComponent component = DaggerAppComponent
                .builder()
                .context(this)
                .build();

        component.inject(this);

        JobManager.create(this).addJobCreator(component.getJobCreator());

        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(this, getString(R.string.app_unit_id));
    }


    @Override
    public AndroidInjector<Activity> activityInjector() {
        return activityInjector;
    }

}
