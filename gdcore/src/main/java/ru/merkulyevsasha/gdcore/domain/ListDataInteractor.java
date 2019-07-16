package ru.merkulyevsasha.gdcore.domain;


import java.util.List;

import io.reactivex.Single;
import ru.merkulyevsasha.gdcore.models.ListData;

public interface ListDataInteractor {

    Single<List<ListData>> getList(String tableName);

}
