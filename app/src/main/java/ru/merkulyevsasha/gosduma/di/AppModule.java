package ru.merkulyevsasha.gosduma.di;

import android.content.Context;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private final Context mAppContext;

    public AppModule(Context appContext){
        mAppContext = appContext;
    }

    @Singleton
    @Provides
    Context providesContext() {
        return mAppContext;
    }

    @Singleton
    @Provides
    ExecutorService providesExecutorService() {
        return Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() );
    }

}
