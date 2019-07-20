package ru.merkulyevsasha.gddata;


import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Single;
import ru.merkulyevsasha.gdcore.database.GDDatabaseRepository;
import ru.merkulyevsasha.gdcore.models.Law;
import ru.merkulyevsasha.gdcore.repositories.LawsRepository;

public class LawsRepositoryImpl implements LawsRepository {

    private GDDatabaseRepository db;

    LawsRepositoryImpl(GDDatabaseRepository db) {
        this.db = db;
    }

    @Override
    public Single<List<Law>> getLaws2(final String search, final String order) {
        return Single.fromCallable(new Callable<List<Law>>() {
            @Override
            public List<Law> call() throws Exception {
                return db.getLaws(search, order);
            }
        });
    }

}
