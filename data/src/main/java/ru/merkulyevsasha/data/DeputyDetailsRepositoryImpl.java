package ru.merkulyevsasha.data;


import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Single;
import ru.merkulyevsasha.gdcore.database.GDDatabaseRepository;
import ru.merkulyevsasha.gdcore.models.Law;
import ru.merkulyevsasha.gdcore.repositories.DeputyDetailsRepository;

public class DeputyDetailsRepositoryImpl implements DeputyDetailsRepository {

    private GDDatabaseRepository db;

    DeputyDetailsRepositoryImpl(GDDatabaseRepository db) {
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
