package ru.merkulyevsasha.data;


import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Single;
import ru.merkulyevsasha.gdcore.database.DatabaseHelper;
import ru.merkulyevsasha.gdcore.models.DeputyRequest;
import ru.merkulyevsasha.gdcore.repositories.DeputyRequestsRepository;

public class DeputyRequestsRepositoryImpl implements DeputyRequestsRepository {

    private DatabaseHelper db;

    DeputyRequestsRepositoryImpl(DatabaseHelper db) {
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
