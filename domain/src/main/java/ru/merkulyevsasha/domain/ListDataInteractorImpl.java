package ru.merkulyevsasha.domain;


import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import ru.merkulyevsasha.gdcore.domain.ListDataInteractor;
import ru.merkulyevsasha.gdcore.models.ListData;
import ru.merkulyevsasha.gdcore.repositories.ListDataRepository;

public class ListDataInteractorImpl implements ListDataInteractor {

    private final ListDataRepository repo;
    private final Scheduler scheduler;

    ListDataInteractorImpl(ListDataRepository repo, Scheduler scheduler) {
        this.repo = repo;
        this.scheduler = scheduler;
    }

    @Override
    public Single<List<ListData>> getList(String tableName) {
        return repo.select2(tableName)
            .subscribeOn(scheduler);
    }
}
