package ru.merkulyevsasha.gosduma.domain;


import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import ru.merkulyevsasha.gosduma.R;
import ru.merkulyevsasha.gosduma.data.ClickCounterRepository;
import ru.merkulyevsasha.gosduma.data.NewsRepository;
import ru.merkulyevsasha.gosduma.data.http.RssParser;
import ru.merkulyevsasha.gosduma.models.Article;

public class NewsInteractorImpl implements NewsInteractor {

    private final NewsRepository repo;
    private final ClickCounterRepository clickRepo;
    private final Scheduler scheduler;

    public NewsInteractorImpl(NewsRepository repo, ClickCounterRepository clickRepo, Scheduler scheduler){
        this.repo = repo;
        this.scheduler = scheduler;
        this.clickRepo = clickRepo;
    }

    public Call<ResponseBody> getCallResponseBody(int key) {
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
    public Single<List<Article>> getArticles(final int id) {
        return Single.fromCallable(new Callable<List<Article>>() {
            @Override
            public List<Article> call() throws Exception {
                return repo.getArticles(id);
            }
        }).subscribeOn(scheduler);
    }

    @Override
    public Single<List<Article>> getNews(final int id) {
        return Single.fromCallable(new Callable<List<Article>>() {
            @Override
            public List<Article> call() throws Exception {
                Call<ResponseBody> resp = getCallResponseBody(id);
                Response<ResponseBody> response = resp.execute();
                RssParser parser = new RssParser();
                return parser.parseXml(response.body().string());
            }
        }).doOnSuccess(new Consumer<List<Article>>() {
            @Override
            public void accept(@NonNull List<Article> articles) throws Exception {
                repo.saveToCache(id, articles);
            }
        }).subscribeOn(scheduler);
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
