package ru.merkulyevsasha.gosduma.data;

import java.util.List;

import ru.merkulyevsasha.gosduma.data.db.DatabaseHelper;
import ru.merkulyevsasha.gosduma.models.ListData;


public class ListDataRepositoryImpl implements ListDataRepository {

    private DatabaseHelper db;

    public ListDataRepositoryImpl(DatabaseHelper db){
        this.db = db;
    }

    @Override
    public List<ListData> select(String tableName) {
        return db.selectAll(tableName);
    }
}
