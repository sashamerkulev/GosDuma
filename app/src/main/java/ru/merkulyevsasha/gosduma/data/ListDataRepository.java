package ru.merkulyevsasha.gosduma.data;

import java.util.List;

import ru.merkulyevsasha.gosduma.models.ListData;


public interface ListDataRepository {

    List<ListData> select(String tableName);

}
