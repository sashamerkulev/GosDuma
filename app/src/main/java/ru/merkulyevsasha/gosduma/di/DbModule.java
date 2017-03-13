package ru.merkulyevsasha.gosduma.di;


import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.merkulyevsasha.gosduma.data.db.DatabaseHelper;
import ru.merkulyevsasha.gosduma.data.http.AktRssService;
import ru.merkulyevsasha.gosduma.data.http.NewsRssService;
import ru.merkulyevsasha.gosduma.data.preferences.SettingsSharedPreferences;

@Module
public class DbModule {

    @Singleton
    @Provides
    DatabaseHelper providesDatabaseHelper(Context context) {
        return new DatabaseHelper(DatabaseHelper.getDbPath(context));
    }

    @Singleton
    @Provides
    SettingsSharedPreferences providesSettingsSharedPreferences(Context context) {
        return new SettingsSharedPreferences(context);
    }


    @Singleton
    @Provides
    NewsRssService providesRssService() {
        return new NewsRssService();
    }

    @Singleton
    @Provides
    AktRssService providesAktRssService() {
        return new AktRssService();
    }


}
