package ru.merkulyevsasha.gosduma.domain;


import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import ru.merkulyevsasha.gosduma.data.DeputyRequestsRepository;
import ru.merkulyevsasha.gosduma.models.DeputyRequest;

public class DeputyRequestsInteractorImpl implements DeputyRequestsInteractor {

    private final DeputyRequestsRepository repo;
    private final Scheduler scheduler;

    public DeputyRequestsInteractorImpl(DeputyRequestsRepository repo, Scheduler scheduler){
        this.repo = repo;
        this.scheduler = scheduler;
    }

    @Override
    public Single<List<DeputyRequest>> getDeputyRequests(String searchText, String orderBy) {
        return repo.getDeputyRequest2(searchText, orderBy)
                .subscribeOn(scheduler);
    }
}
