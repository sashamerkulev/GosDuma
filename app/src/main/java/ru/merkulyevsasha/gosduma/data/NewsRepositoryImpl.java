package ru.merkulyevsasha.gosduma.data;


import java.util.List;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import ru.merkulyevsasha.gosduma.data.db.DatabaseHelper;
import ru.merkulyevsasha.gosduma.data.http.AktRssService;
import ru.merkulyevsasha.gosduma.data.http.NewsRssService;
import ru.merkulyevsasha.gosduma.models.Article;

public class NewsRepositoryImpl implements NewsRepository {

    private DatabaseHelper db;
    private NewsRssService serv;
    private AktRssService akt;

    @Inject
    NewsRepositoryImpl(DatabaseHelper db, NewsRssService service, AktRssService aktservice){
        this.db = db;
        this.serv = service;
        this.akt = aktservice;
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


    @Override
    public Call<ResponseBody> aktPresident(){
        return akt.aktPresident();
    }

    @Override
    public Call<ResponseBody> aktGoverment(){
        return akt.aktGoverment();
    }

    @Override
    public Call<ResponseBody> aktSovetfed(){
        return akt.aktSovetfed();
    }

    @Override
    public Call<ResponseBody> aktGosduma(){
        return akt.aktGosduma();
    }



}
