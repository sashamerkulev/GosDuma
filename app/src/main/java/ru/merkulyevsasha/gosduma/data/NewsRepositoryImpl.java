package ru.merkulyevsasha.gosduma.data;


import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import ru.merkulyevsasha.gosduma.data.db.DatabaseHelper;
import ru.merkulyevsasha.gosduma.data.http.RssService;
import ru.merkulyevsasha.gosduma.models.Article;

public class NewsRepositoryImpl implements NewsRepository {

    private DatabaseHelper db;
    private RssService serv;

    public NewsRepositoryImpl(DatabaseHelper db, RssService service){
        this.db = db;
        this.serv = service;
    }

    @Override
    public List<Article> getArticles(int id) {
        return db.getArticles(id);
    }

    @Override
    public void saveToCache(int id, List<Article> result){
        db.deleteArticles(id);
        db.addArticles(id, result);
    }

    @Override
    public Call<ResponseBody> gosduma() {
        return serv.gosduma();
    }

    @Override
    public Call<ResponseBody> chairman() {
        return serv.chairman();
    }
}
