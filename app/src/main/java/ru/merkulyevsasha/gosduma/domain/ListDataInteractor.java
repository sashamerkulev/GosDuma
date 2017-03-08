package ru.merkulyevsasha.gosduma.domain;


import java.util.List;

import ru.merkulyevsasha.gosduma.models.ListData;

public interface ListDataInteractor {

    List<ListData> select(String tableName);

}
