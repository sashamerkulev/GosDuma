package ru.merkulyevsasha.gosduma.domain;


import java.util.List;

import ru.merkulyevsasha.gosduma.data.ListDataRepository;
import ru.merkulyevsasha.gosduma.models.ListData;

public class ListDataInteractorImpl implements ListDataInteractor {

    private ListDataRepository repo;

    public ListDataInteractorImpl(ListDataRepository repo){
        this.repo = repo;
    }

    @Override
    public List<ListData> select(String tableName) {
        return repo.select(tableName);
    }
}
