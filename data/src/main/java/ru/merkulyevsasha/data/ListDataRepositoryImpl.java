package ru.merkulyevsasha.data;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Single;
import ru.merkulyevsasha.gdcore.database.GDDatabaseRepository;
import ru.merkulyevsasha.gdcore.models.ListData;
import ru.merkulyevsasha.gdcore.repositories.ListDataRepository;


public class ListDataRepositoryImpl implements ListDataRepository {

    private GDDatabaseRepository db;

    ListDataRepositoryImpl(GDDatabaseRepository db) {
        this.db = db;
    }

    @Override
    public Single<List<ListData>> select2(final String tableName) {
        return Single.fromCallable(new Callable<List<ListData>>() {
            @Override
            public List<ListData> call() throws Exception {
                return db.selectAll(tableName);
            }
        });
    }
}
