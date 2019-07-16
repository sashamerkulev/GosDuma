package ru.merkulyevsasha.data;


import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Single;
import ru.merkulyevsasha.gdcore.database.DatabaseHelper;
import ru.merkulyevsasha.gdcore.models.Deputy;
import ru.merkulyevsasha.gdcore.repositories.DeputiesRepository;

public class DeputiesRepositoryImpl implements DeputiesRepository {

    private DatabaseHelper db;

    DeputiesRepositoryImpl(DatabaseHelper db) {
        this.db = db;
    }

    @Override
    public Single<List<Deputy>> getDeputies2(final String search, final String order, final String position, final int isCurrent) {
        return Single.fromCallable(new Callable<List<Deputy>>() {
            @Override
            public List<Deputy> call() throws Exception {
                return db.getDeputies(search, order, position, isCurrent);
            }
        });
    }
}
