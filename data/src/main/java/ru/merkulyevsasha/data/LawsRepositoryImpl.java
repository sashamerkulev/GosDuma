package ru.merkulyevsasha.data;


import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Single;
import ru.merkulyevsasha.gdcore.database.DatabaseHelper;
import ru.merkulyevsasha.gdcore.models.Law;
import ru.merkulyevsasha.gdcore.repositories.LawsRepository;

public class LawsRepositoryImpl implements LawsRepository {

    private DatabaseHelper db;

    LawsRepositoryImpl(DatabaseHelper db) {
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
