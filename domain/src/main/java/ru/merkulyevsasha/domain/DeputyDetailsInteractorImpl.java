package ru.merkulyevsasha.domain;


import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import ru.merkulyevsasha.gdcore.domain.DeputyDetailsInteractor;
import ru.merkulyevsasha.gdcore.models.Law;
import ru.merkulyevsasha.gdcore.repositories.DeputyDetailsRepository;

public class DeputyDetailsInteractorImpl implements DeputyDetailsInteractor {

    private final DeputyDetailsRepository repo;
    private final Scheduler scheduler;

    DeputyDetailsInteractorImpl(DeputyDetailsRepository repo, Scheduler scheduler) {
        this.repo = repo;
        this.scheduler = scheduler;
    }

    @Override
    public Single<List<Law>> getDeputyLaws(int deputyId, String searchText, String orderby) {
        return repo.getDeputyLaws2(deputyId, searchText, orderby)
            .subscribeOn(scheduler);
    }


}
