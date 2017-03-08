package ru.merkulyevsasha.gosduma.di;


import java.util.concurrent.ExecutorService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.merkulyevsasha.gosduma.data.DeputiesRepository;
import ru.merkulyevsasha.gosduma.data.LawsRepository;
import ru.merkulyevsasha.gosduma.data.ListDataRepository;
import ru.merkulyevsasha.gosduma.data.NewsRepository;
import ru.merkulyevsasha.gosduma.domain.DeputiesInteractor;
import ru.merkulyevsasha.gosduma.domain.DeputiesInteractorImpl;
import ru.merkulyevsasha.gosduma.domain.LawsInteractor;
import ru.merkulyevsasha.gosduma.domain.LawsInteractorImpl;
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

    @Singleton
    @Provides
    LawsInteractor providesLawsInteractor(ExecutorService serv, LawsRepository repo) {
        return new LawsInteractorImpl(serv, repo);
    }

    @Singleton
    @Provides
    DeputiesInteractor providesDeputiesInteractor(ExecutorService serv, DeputiesRepository repo) {
        return new DeputiesInteractorImpl(serv, repo);
    }

}
