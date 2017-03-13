package ru.merkulyevsasha.gosduma.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.merkulyevsasha.gosduma.data.ClickCounterRepository;
import ru.merkulyevsasha.gosduma.data.ClickCounterRepositoryImpl;
import ru.merkulyevsasha.gosduma.data.DeputiesRepository;
import ru.merkulyevsasha.gosduma.data.DeputiesRepositoryImpl;
import ru.merkulyevsasha.gosduma.data.DeputyDetailsRepository;
import ru.merkulyevsasha.gosduma.data.DeputyDetailsRepositoryImpl;
import ru.merkulyevsasha.gosduma.data.DeputyRequestsRepository;
import ru.merkulyevsasha.gosduma.data.DeputyRequestsRepositoryImpl;
import ru.merkulyevsasha.gosduma.data.LawDetailsRepository;
import ru.merkulyevsasha.gosduma.data.LawDetailsRepositoryImpl;
import ru.merkulyevsasha.gosduma.data.LawsRepository;
import ru.merkulyevsasha.gosduma.data.LawsRepositoryImpl;
import ru.merkulyevsasha.gosduma.data.ListDataRepository;
import ru.merkulyevsasha.gosduma.data.ListDataRepositoryImpl;
import ru.merkulyevsasha.gosduma.data.NewsRepository;
import ru.merkulyevsasha.gosduma.data.NewsRepositoryImpl;
import ru.merkulyevsasha.gosduma.data.db.DatabaseHelper;
import ru.merkulyevsasha.gosduma.data.http.AktRssService;
import ru.merkulyevsasha.gosduma.data.http.NewsRssService;
import ru.merkulyevsasha.gosduma.data.preferences.SettingsSharedPreferences;


@Module
public class RepositoriesModule {

    @Singleton
    @Provides
    NewsRepository providesNewsRepository(DatabaseHelper db, NewsRssService service, AktRssService akt) {
        return new NewsRepositoryImpl(db, service, akt);
    }

    @Singleton
    @Provides
    ListDataRepository providesListDataRepository(DatabaseHelper db) {
        return new ListDataRepositoryImpl(db);
    }

    @Singleton
    @Provides
    LawsRepository providesLawsRepository(DatabaseHelper db) {
        return new LawsRepositoryImpl(db);
    }

    @Singleton
    @Provides
    DeputiesRepository providesDeputiesRepository(DatabaseHelper db) {
        return new DeputiesRepositoryImpl(db);
    }

    @Singleton
    @Provides
    DeputyRequestsRepository providesDeputyRequestsRepository(DatabaseHelper db) {
        return new DeputyRequestsRepositoryImpl(db);
    }

    @Singleton
    @Provides
    LawDetailsRepository providesLawDetailsRepository(DatabaseHelper db) {
        return new LawDetailsRepositoryImpl(db);
    }

    @Singleton
    @Provides
    DeputyDetailsRepository providesDeputyDetailsRepository(DatabaseHelper db) {
        return new DeputyDetailsRepositoryImpl(db);
    }

    @Singleton
    @Provides
    ClickCounterRepository providesClickCounterRepository(SettingsSharedPreferences pref) {
        return new ClickCounterRepositoryImpl(pref);
    }

}
