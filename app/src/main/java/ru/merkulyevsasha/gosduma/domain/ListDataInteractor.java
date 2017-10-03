package ru.merkulyevsasha.gosduma.domain;


import java.util.List;

import io.reactivex.Single;
import ru.merkulyevsasha.gosduma.models.ListData;

public interface ListDataInteractor {

    Single<List<ListData>> getList(String tableName);

}
