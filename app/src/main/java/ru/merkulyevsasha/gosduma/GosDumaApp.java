package ru.merkulyevsasha.gosduma;

import android.app.Application;

import com.evernote.android.job.JobManager;
import com.google.android.gms.ads.MobileAds;

import ru.merkulyevsasha.gosduma.di.AppModule;
import ru.merkulyevsasha.gosduma.di.DaggerDbComponent;
import ru.merkulyevsasha.gosduma.di.DbComponent;
import ru.merkulyevsasha.gosduma.di.DbModule;
import ru.merkulyevsasha.gosduma.di.InteractorsModule;
import ru.merkulyevsasha.gosduma.di.PresentersModule;
import ru.merkulyevsasha.gosduma.di.RepositoriesModule;
import ru.merkulyevsasha.gosduma.presentation.services.GDJobCreator;


public class GosDumaApp extends Application {

    private static DbComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerDbComponent.builder()
                .appModule(new AppModule(this))
                .dbModule(new DbModule())
                .repositoriesModule(new RepositoriesModule())
                .interactorsModule(new InteractorsModule())
                .presentersModule(new PresentersModule())
                .build();
        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(this, getString(R.string.app_unit_id));
        JobManager.create(this).addJobCreator(new GDJobCreator());
    }

    public static DbComponent getComponent() {
        return component;
    }

}
