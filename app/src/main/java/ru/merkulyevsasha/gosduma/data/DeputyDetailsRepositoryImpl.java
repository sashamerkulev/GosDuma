package ru.merkulyevsasha.gosduma.data;


import java.util.List;

import ru.merkulyevsasha.gosduma.data.db.DatabaseHelper;
import ru.merkulyevsasha.gosduma.models.Law;

public class DeputyDetailsRepositoryImpl implements DeputyDetailsRepository{

    private DatabaseHelper db;

    public DeputyDetailsRepositoryImpl(DatabaseHelper db){
        this.db = db;
    }

    @Override
    public List<Law> getDeputyLaws(int deputyId, String searchText, String orderby) {
        return db.getDeputyLaws(deputyId, searchText, orderby);
    }
}
