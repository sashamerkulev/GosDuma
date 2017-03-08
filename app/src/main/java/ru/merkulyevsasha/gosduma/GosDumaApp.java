package ru.merkulyevsasha.gosduma;

import android.app.Application;

import ru.merkulyevsasha.gosduma.di.AppModule;
import ru.merkulyevsasha.gosduma.di.DaggerDbComponent;
import ru.merkulyevsasha.gosduma.di.DbComponent;
import ru.merkulyevsasha.gosduma.di.DbModule;
import ru.merkulyevsasha.gosduma.di.InteractorsModule;
import ru.merkulyevsasha.gosduma.di.PresentersModule;
import ru.merkulyevsasha.gosduma.di.RepositoriesModule;


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
    }

    public static DbComponent getComponent() {
        return component;
    }

}
