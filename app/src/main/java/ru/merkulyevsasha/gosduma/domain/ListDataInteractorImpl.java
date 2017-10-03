package ru.merkulyevsasha.gosduma.domain;


import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import ru.merkulyevsasha.gosduma.data.ListDataRepository;
import ru.merkulyevsasha.gosduma.models.ListData;

public class ListDataInteractorImpl implements ListDataInteractor {

    private final ListDataRepository repo;
    private final Scheduler scheduler;

    public ListDataInteractorImpl(ListDataRepository repo, Scheduler scheduler){
        this.repo = repo;
        this.scheduler = scheduler;
    }

    @Override
    public Single<List<ListData>> getList(String tableName) {
        return repo.select2(tableName)
                .subscribeOn(scheduler);
    }
}
