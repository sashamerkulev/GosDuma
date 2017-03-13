package ru.merkulyevsasha.gosduma.di;


import java.util.concurrent.ExecutorService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.merkulyevsasha.gosduma.data.ClickCounterRepository;
import ru.merkulyevsasha.gosduma.data.DeputiesRepository;
import ru.merkulyevsasha.gosduma.data.DeputyDetailsRepository;
import ru.merkulyevsasha.gosduma.data.DeputyRequestsRepository;
import ru.merkulyevsasha.gosduma.data.LawDetailsRepository;
import ru.merkulyevsasha.gosduma.data.LawsRepository;
import ru.merkulyevsasha.gosduma.data.ListDataRepository;
import ru.merkulyevsasha.gosduma.data.NewsRepository;
import ru.merkulyevsasha.gosduma.domain.DeputiesInteractor;
import ru.merkulyevsasha.gosduma.domain.DeputiesInteractorImpl;
import ru.merkulyevsasha.gosduma.domain.DeputyDetailsInteractor;
import ru.merkulyevsasha.gosduma.domain.DeputyDetailsInteractorImpl;
import ru.merkulyevsasha.gosduma.domain.DeputyRequestsInteractor;
import ru.merkulyevsasha.gosduma.domain.DeputyRequestsInteractorImpl;
import ru.merkulyevsasha.gosduma.domain.LawDetailsInteractor;
import ru.merkulyevsasha.gosduma.domain.LawDetailsInteractorImpl;
import ru.merkulyevsasha.gosduma.domain.LawsInteractor;
import ru.merkulyevsasha.gosduma.domain.LawsInteractorImpl;
import ru.merkulyevsasha.gosduma.domain.ListDataInteractor;
import ru.merkulyevsasha.gosduma.domain.ListDataInteractorImpl;
import ru.merkulyevsasha.gosduma.domain.NewsInteractor;
import ru.merkulyevsasha.gosduma.domain.NewsInteractorImpl;
import ru.merkulyevsasha.gosduma.domain.NewsServiceInteractor;
import ru.merkulyevsasha.gosduma.domain.NewsServiceInteractorImpl;


@Module
public class InteractorsModule {

    @Singleton
    @Provides
    NewsInteractor providesNewsRepository(ExecutorService serv,NewsRepository repo, ClickCounterRepository clickRepo) {
        return new NewsInteractorImpl(serv, repo, clickRepo);
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

    @Singleton
    @Provides
    DeputyRequestsInteractor providesDeputyRequestsInteractor(ExecutorService serv, DeputyRequestsRepository repo) {
        return new DeputyRequestsInteractorImpl(serv, repo);
    }

    @Singleton
    @Provides
    LawDetailsInteractor providesLawDetailsInteractor(ExecutorService serv, LawDetailsRepository repo) {
        return new LawDetailsInteractorImpl(serv, repo);
    }

    @Singleton
    @Provides
    DeputyDetailsInteractor providesDeputyDetailsInteractor(ExecutorService serv, DeputyDetailsRepository repo) {
        return new DeputyDetailsInteractorImpl(serv, repo);
    }

    @Singleton
    @Provides
    NewsServiceInteractor providesNewsServiceInteractor(NewsRepository repo) {
        return new NewsServiceInteractorImpl(repo);
    }

}
