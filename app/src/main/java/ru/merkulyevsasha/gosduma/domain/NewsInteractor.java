package ru.merkulyevsasha.gosduma.domain;


import java.util.List;

import ru.merkulyevsasha.gosduma.models.Article;

public interface NewsInteractor {

    interface NewsCallback{

        void success(List<Article> articles);
        void failure(Exception e);

    }

    List<Article> getArticles(int id);
    void loadNews(int id, NewsCallback callback);

    boolean canShowInterstitialAd();
    void resetCounter();
    void incrementCounter();

}
