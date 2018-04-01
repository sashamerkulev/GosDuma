package ru.merkulyevsasha.gosduma.domain;


import java.util.List;

import javax.inject.Inject;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import ru.merkulyevsasha.gosduma.data.DeputiesRepository;
import ru.merkulyevsasha.gosduma.models.Deputy;

public class DeputiesInteractorImpl implements DeputiesInteractor {

    private final DeputiesRepository repo;
    private final Scheduler scheduler;

    @Inject
    DeputiesInteractorImpl(DeputiesRepository repo, Scheduler scheduler){
        this.repo = repo;
        this.scheduler = scheduler;
    }

    @Override
    public Single<List<Deputy>> getDeputies(String searchText, String orderBy, String position, int isCurrent) {
        return repo.getDeputies2(searchText, orderBy, position, isCurrent)
                .subscribeOn(scheduler);
    }
}
