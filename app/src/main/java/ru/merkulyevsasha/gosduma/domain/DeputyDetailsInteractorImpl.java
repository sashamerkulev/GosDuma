package ru.merkulyevsasha.gosduma.domain;


import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import ru.merkulyevsasha.gosduma.data.DeputyDetailsRepository;
import ru.merkulyevsasha.gosduma.models.Law;

public class DeputyDetailsInteractorImpl implements DeputyDetailsInteractor{

    private final DeputyDetailsRepository repo;
    private final Scheduler scheduler;

    public DeputyDetailsInteractorImpl(DeputyDetailsRepository repo, Scheduler scheduler){
        this.repo = repo;
        this.scheduler = scheduler;
    }

    @Override
    public Single<List<Law>> getDeputyLaws(int deputyId, String searchText, String orderby) {
        return repo.getDeputyLaws2(deputyId, searchText, orderby)
                .subscribeOn(scheduler);
    }


}
