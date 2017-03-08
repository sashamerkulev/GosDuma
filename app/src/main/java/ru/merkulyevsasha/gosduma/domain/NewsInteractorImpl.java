package ru.merkulyevsasha.gosduma.domain;


import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import ru.merkulyevsasha.gosduma.R;
import ru.merkulyevsasha.gosduma.data.NewsRepository;
import ru.merkulyevsasha.gosduma.models.Article;

public class NewsInteractorImpl implements NewsInteractor {

    private NewsRepository repo;

    public NewsInteractorImpl(NewsRepository repo){
        this.repo = repo;
    }

    @Override
    public List<Article> getArticles(int id) {
        return repo.getArticles(id);
    }

    @Override
    public Call<ResponseBody> news(int id) {
        if (id == R.id.nav_news_gd){
            return repo.gosduma();
        } else if (id == R.id.nav_news_preds){
            return repo.chairman();
        }
        return null;
    }

    @Override
    public void saveToCache(int id, List<Article> result){
        repo.saveToCache(id, result);
    }

}
