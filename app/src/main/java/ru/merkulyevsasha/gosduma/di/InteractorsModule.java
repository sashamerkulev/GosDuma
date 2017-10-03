package ru.merkulyevsasha.gosduma.di;


import java.util.concurrent.ExecutorService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
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
    NewsInteractor providesNewsRepository(NewsRepository repo, ClickCounterRepository clickRepo, Scheduler scheduler) {
        return new NewsInteractorImpl(repo, clickRepo, scheduler);
    }

    @Singleton
    @Provides
    ListDataInteractor providesListDataInteractor(ListDataRepository repo, Scheduler scheduler) {
        return new ListDataInteractorImpl(repo, scheduler);
    }

    @Singleton
    @Provides
    LawsInteractor providesLawsInteractor(LawsRepository repo, Scheduler scheduler) {
        return new LawsInteractorImpl(repo, scheduler);
    }

    @Singleton
    @Provides
    DeputiesInteractor providesDeputiesInteractor(DeputiesRepository repo, Scheduler scheduler) {
        return new DeputiesInteractorImpl(repo, scheduler);
    }

    @Singleton
    @Provides
    DeputyRequestsInteractor providesDeputyRequestsInteractor(DeputyRequestsRepository repo, Scheduler scheduler) {
        return new DeputyRequestsInteractorImpl(repo, scheduler);
    }

    @Singleton
    @Provides
    LawDetailsInteractor providesLawDetailsInteractor(LawDetailsRepository repo, Scheduler scheduler) {
        return new LawDetailsInteractorImpl(repo, scheduler);
    }

    @Singleton
    @Provides
    DeputyDetailsInteractor providesDeputyDetailsInteractor(DeputyDetailsRepository repo, Scheduler scheduler) {
        return new DeputyDetailsInteractorImpl(repo, scheduler);
    }

    @Singleton
    @Provides
    NewsServiceInteractor providesNewsServiceInteractor(NewsRepository repo, NewsInteractor newsInteractor, Scheduler scheduler) {
        return new NewsServiceInteractorImpl(repo, newsInteractor, scheduler);
    }

}
