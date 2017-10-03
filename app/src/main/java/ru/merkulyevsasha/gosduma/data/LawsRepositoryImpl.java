package ru.merkulyevsasha.gosduma.data;


import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Single;
import ru.merkulyevsasha.gosduma.data.db.DatabaseHelper;
import ru.merkulyevsasha.gosduma.models.Law;

public class LawsRepositoryImpl implements LawsRepository{

    private DatabaseHelper db;

    public LawsRepositoryImpl(DatabaseHelper db){
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
