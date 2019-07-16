package ru.merkulyevsasha.domain;


import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import ru.merkulyevsasha.gdcore.domain.DeputiesInteractor;
import ru.merkulyevsasha.gdcore.models.Deputy;
import ru.merkulyevsasha.gdcore.repositories.DeputiesRepository;

public class DeputiesInteractorImpl implements DeputiesInteractor {

    private final DeputiesRepository repo;
    private final Scheduler scheduler;

    DeputiesInteractorImpl(DeputiesRepository repo, Scheduler scheduler) {
        this.repo = repo;
        this.scheduler = scheduler;
    }

    @Override
    public Single<List<Deputy>> getDeputies(String searchText, String orderBy, String position, int isCurrent) {
        return repo.getDeputies2(searchText, orderBy, position, isCurrent)
            .subscribeOn(scheduler);
    }
}
