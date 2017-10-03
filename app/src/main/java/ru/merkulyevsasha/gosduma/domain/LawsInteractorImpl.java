package ru.merkulyevsasha.gosduma.domain;


import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import ru.merkulyevsasha.gosduma.data.LawsRepository;
import ru.merkulyevsasha.gosduma.models.Law;

public class LawsInteractorImpl implements LawsInteractor {

    private final LawsRepository repo;
    private final Scheduler scheduler;

    public LawsInteractorImpl(LawsRepository repo, Scheduler scheduler){
        this.repo = repo;
        this.scheduler = scheduler;
    }

    @Override
    public Single<List<Law>> getLaws(String searchText, String orderBy) {
        return repo.getLaws2(searchText, orderBy)
                .subscribeOn(scheduler);
    }
}
