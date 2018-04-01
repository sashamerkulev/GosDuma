package ru.merkulyevsasha.gosduma.data;


import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import io.reactivex.Single;
import ru.merkulyevsasha.gosduma.data.db.DatabaseHelper;
import ru.merkulyevsasha.gosduma.models.Deputy;

public class DeputiesRepositoryImpl implements DeputiesRepository {

    private DatabaseHelper db;

    @Inject
    DeputiesRepositoryImpl(DatabaseHelper db){
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
