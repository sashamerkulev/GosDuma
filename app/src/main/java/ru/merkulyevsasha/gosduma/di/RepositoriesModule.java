package ru.merkulyevsasha.gosduma.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.merkulyevsasha.gosduma.data.NewsRepository;
import ru.merkulyevsasha.gosduma.data.NewsRepositoryImpl;
import ru.merkulyevsasha.gosduma.data.db.DatabaseHelper;
import ru.merkulyevsasha.gosduma.data.http.RssService;


@Module
public class RepositoriesModule {

    @Singleton
    @Provides
    NewsRepository providesNewsRepository(DatabaseHelper db, RssService service) {
        return new NewsRepositoryImpl(db, service);
    }


}
