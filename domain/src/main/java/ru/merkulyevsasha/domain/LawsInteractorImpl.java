package ru.merkulyevsasha.domain;


import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import ru.merkulyevsasha.gdcore.domain.LawsInteractor;
import ru.merkulyevsasha.gdcore.models.Law;
import ru.merkulyevsasha.gdcore.repositories.LawsRepository;

public class LawsInteractorImpl implements LawsInteractor {

    private final LawsRepository repo;
    private final Scheduler scheduler;

    LawsInteractorImpl(LawsRepository repo, Scheduler scheduler) {
        this.repo = repo;
        this.scheduler = scheduler;
    }

    @Override
    public Single<List<Law>> getLaws(String searchText, String orderBy) {
        return repo.getLaws2(searchText, orderBy)
            .subscribeOn(scheduler);
    }
}
