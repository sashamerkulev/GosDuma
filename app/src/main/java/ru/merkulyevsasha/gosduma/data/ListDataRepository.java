package ru.merkulyevsasha.gosduma.data;

import java.util.List;

import io.reactivex.Single;
import ru.merkulyevsasha.gosduma.models.ListData;


public interface ListDataRepository {

    Single<List<ListData>> select2(String tableName);
}
