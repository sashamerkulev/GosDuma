package ru.merkulyevsasha.gosduma.data;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Single;
import ru.merkulyevsasha.gosduma.data.db.DatabaseHelper;
import ru.merkulyevsasha.gosduma.models.ListData;


public class ListDataRepositoryImpl implements ListDataRepository {

    private DatabaseHelper db;

    public ListDataRepositoryImpl(DatabaseHelper db){
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
