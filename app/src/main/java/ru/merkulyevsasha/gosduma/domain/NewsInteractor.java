package ru.merkulyevsasha.gosduma.domain;


import java.util.List;

import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.Call;
import ru.merkulyevsasha.gosduma.models.Article;

public interface NewsInteractor {

    Call<ResponseBody> getCallResponseBody(int key);

    Single<List<Article>> getArticles(int id);
    Single<List<Article>> getNews(int id);

    boolean canShowInterstitialAd();
    void resetCounter();
    void incrementCounter();

}
