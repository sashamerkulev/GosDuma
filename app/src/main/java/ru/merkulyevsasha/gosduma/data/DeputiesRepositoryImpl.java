package ru.merkulyevsasha.gosduma.data;


import java.util.List;

import ru.merkulyevsasha.gosduma.data.db.DatabaseHelper;
import ru.merkulyevsasha.gosduma.models.Deputy;

public class DeputiesRepositoryImpl implements DeputiesRepository {

    private DatabaseHelper db;

    public DeputiesRepositoryImpl(DatabaseHelper db){
        this.db = db;
    }

    @Override
    public List<Deputy> getDeputies(String search, String order, String position, int isCurrent) {
        return db.getDeputies(search, order, position, isCurrent);
    }
}
