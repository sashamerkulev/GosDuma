package ru.merkulyevsasha.gdcore.repositories;

import java.util.List;

import io.reactivex.Single;
import ru.merkulyevsasha.gdcore.models.ListData;


public interface ListDataRepository {

    Single<List<ListData>> select2(String tableName);
}
