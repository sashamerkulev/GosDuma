package ru.merkulyevsasha.gosduma.domain;


import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.merkulyevsasha.gosduma.R;
import ru.merkulyevsasha.gosduma.data.ClickCounterRepository;
import ru.merkulyevsasha.gosduma.data.NewsRepository;
import ru.merkulyevsasha.gosduma.data.http.RssParser;
import ru.merkulyevsasha.gosduma.models.Article;

public class NewsInteractorImpl implements NewsInteractor {

    protected NewsRepository repo;
    protected ClickCounterRepository clickRepo;

    public NewsInteractorImpl(NewsRepository repo, ClickCounterRepository clickRepo){
        this.repo = repo;
        this.clickRepo = clickRepo;
    }

    @Override
    public List<Article> getArticles(int id) {
        return repo.getArticles(id);
    }

    protected Call<ResponseBody> getCallResponseBody(int key) {
        Call<ResponseBody> resp = null;
        if (key == R.id.nav_news_gd) {
            resp = repo.gosduma();
        } else if (key == R.id.nav_news_preds) {
            resp = repo.chairman();
        } else if (key == R.id.nav_akt_pres) {
            resp = repo.aktPresident();
        } else if (key == R.id.nav_akt_gover) {
            resp = repo.aktGoverment();
        } else if (key == R.id.nav_akt_sf) {
            resp = repo.aktSovetfed();
        } else if (key == R.id.nav_akt_gd) {
            resp = repo.aktGosduma();
        }
        return resp;
    }

    @Override
    public void loadNews(final int id, final NewsCallback callback) {

        Call<ResponseBody> resp = getCallResponseBody(id);

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

    @Override
    public boolean canShowInterstitialAd(){
        return clickRepo.canShowInterstitialAd();
    }

    @Override
    public void resetCounter(){
        clickRepo.resetCounter();
    }

    @Override
    public void incrementCounter(){
        clickRepo.incrementCounter();
    }


}
