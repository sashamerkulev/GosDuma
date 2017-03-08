package ru.merkulyevsasha.gosduma.di;


import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.merkulyevsasha.gosduma.data.ListDataRepository;
import ru.merkulyevsasha.gosduma.data.NewsRepository;
import ru.merkulyevsasha.gosduma.domain.ListDataInteractor;
import ru.merkulyevsasha.gosduma.domain.ListDataInteractorImpl;
import ru.merkulyevsasha.gosduma.domain.NewsInteractor;
import ru.merkulyevsasha.gosduma.domain.NewsInteractorImpl;

@Module
public class InteractorsModule {

    @Singleton
    @Provides
    NewsInteractor providesNewsRepository(NewsRepository repo) {
        return new NewsInteractorImpl(repo);
    }

    @Singleton
    @Provides
    ListDataInteractor providesListDataInteractor(ListDataRepository repo) {
        return new ListDataInteractorImpl(repo);
    }


}
