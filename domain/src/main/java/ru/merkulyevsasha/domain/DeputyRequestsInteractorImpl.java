package ru.merkulyevsasha.domain;


import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import ru.merkulyevsasha.gdcore.domain.DeputyRequestsInteractor;
import ru.merkulyevsasha.gdcore.models.DeputyRequest;
import ru.merkulyevsasha.gdcore.repositories.DeputyRequestsRepository;

public class DeputyRequestsInteractorImpl implements DeputyRequestsInteractor {

    private final DeputyRequestsRepository repo;
    private final Scheduler scheduler;

    DeputyRequestsInteractorImpl(DeputyRequestsRepository repo, Scheduler scheduler) {
        this.repo = repo;
        this.scheduler = scheduler;
    }

    @Override
    public Single<List<DeputyRequest>> getDeputyRequests(String searchText, String orderBy) {
        return repo.getDeputyRequest2(searchText, orderBy)
            .subscribeOn(scheduler);
    }
}
