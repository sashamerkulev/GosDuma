package ru.merkulyevsasha.gosduma;

import android.app.Application;

import ru.merkulyevsasha.gosduma.dagger.AppModule;
import ru.merkulyevsasha.gosduma.dagger.DaggerDbComponent;
import ru.merkulyevsasha.gosduma.dagger.DbComponent;
import ru.merkulyevsasha.gosduma.dagger.DbModule;


public class GosDumaApp extends Application {

    DbComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerDbComponent.builder()
                .appModule(new AppModule(this))
                .dbModule(new DbModule())
                .build();
    }

    public DbComponent getComponent() {
        return component;
    }

}
