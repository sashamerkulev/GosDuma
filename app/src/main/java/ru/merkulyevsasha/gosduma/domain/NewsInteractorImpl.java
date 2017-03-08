package ru.merkulyevsasha.gosduma.domain;


import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.merkulyevsasha.gosduma.R;
import ru.merkulyevsasha.gosduma.data.NewsRepository;
import ru.merkulyevsasha.gosduma.data.http.RssParser;
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
    public void loadNews(final int id, final NewsCallback callback) {
        Call<ResponseBody> resp = null;

        if (id == R.id.nav_news_gd){
            resp = repo.gosduma();
        } else if (id == R.id.nav_news_preds){
            resp = repo.chairman();
        }

        if (resp != null){
            resp.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        RssParser parser = new RssParser();
                        List<Article> result = parser.parseXml(response.body().string());
                        repo.saveToCache(id, result);

                        callback.success(result);

                    } catch(Exception e){
                        callback.failure(e);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    callback.failure(new Exception(t));
                }
            });
        }

    }

}
