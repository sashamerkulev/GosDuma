package ru.merkulyevsasha.gosduma.di;


import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.merkulyevsasha.gosduma.data.db.DatabaseHelper;
import ru.merkulyevsasha.gosduma.data.http.RssService;

@Module
public class DbModule {

    @Singleton
    @Provides
    DatabaseHelper providesDatabaseHelper(Context context) {
        return new DatabaseHelper(DatabaseHelper.getDbPath(context));
    }

    @Singleton
    @Provides
    RssService providesRssService() {
        return new RssService();
    }



}
