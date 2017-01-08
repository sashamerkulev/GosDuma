package ru.merkulyevsasha.gosduma.dagger;


import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.merkulyevsasha.gosduma.db.DatabaseHelper;

@Module
public class DbModule {

    @Singleton
    @Provides
    DatabaseHelper providesDatabaseHelper(Context context) {
        return DatabaseHelper.getInstance(DatabaseHelper.getDbPath(context));
    }

}
