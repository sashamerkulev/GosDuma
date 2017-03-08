package ru.merkulyevsasha.gosduma.data;


import java.util.List;

import ru.merkulyevsasha.gosduma.data.db.DatabaseHelper;
import ru.merkulyevsasha.gosduma.models.DeputyRequest;

public class DeputyRequestsRepositoryImpl implements DeputyRequestsRepository {

    private DatabaseHelper db;

    public DeputyRequestsRepositoryImpl(DatabaseHelper db){
        this.db = db;
    }

    @Override
    public List<DeputyRequest> getDeputyRequest(String searchText, String orderBy) {
        return db.getDeputyRequests(searchText, orderBy);
    }
}
