package ru.merkulyevsasha.gosduma.data;


import java.util.List;

import ru.merkulyevsasha.gosduma.data.db.DatabaseHelper;
import ru.merkulyevsasha.gosduma.models.Law;

public class LawsRepositoryImpl implements LawsRepository{

    private DatabaseHelper db;

    public LawsRepositoryImpl(DatabaseHelper db){
        this.db = db;
    }

    @Override
    public List<Law> getLaws(String search, String order){
        return db.getLaws(search, order);
    }

}
