package ru.merkulyevsasha.gosduma.data;


import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Single;
import ru.merkulyevsasha.gosduma.data.db.DatabaseHelper;
import ru.merkulyevsasha.gosduma.models.Law;

public class DeputyDetailsRepositoryImpl implements DeputyDetailsRepository{

    private DatabaseHelper db;

    public DeputyDetailsRepositoryImpl(DatabaseHelper db){
        this.db = db;
    }

    @Override
    public Single<List<Law>> getDeputyLaws2(final int deputyId, final String searchText, final String orderby) {
        return Single.fromCallable(new Callable<List<Law>>() {
            @Override
            public List<Law> call() throws Exception {
                return db.getDeputyLaws(deputyId, searchText, orderby);
            }
        });
    }
}
