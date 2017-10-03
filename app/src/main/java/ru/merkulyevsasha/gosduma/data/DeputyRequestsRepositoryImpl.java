package ru.merkulyevsasha.gosduma.data;


import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Single;
import ru.merkulyevsasha.gosduma.data.db.DatabaseHelper;
import ru.merkulyevsasha.gosduma.models.DeputyRequest;

public class DeputyRequestsRepositoryImpl implements DeputyRequestsRepository {

    private DatabaseHelper db;

    public DeputyRequestsRepositoryImpl(DatabaseHelper db){
        this.db = db;
    }

    @Override
    public Single<List<DeputyRequest>> getDeputyRequest2(final String searchText, final String orderBy) {
        return Single.fromCallable(new Callable<List<DeputyRequest>>() {
            @Override
            public List<DeputyRequest> call() throws Exception {
                return db.getDeputyRequests(searchText, orderBy);
            }
        });
    }
}
