package ru.merkulyevsasha.gosduma.domain;


import java.util.List;

import ru.merkulyevsasha.gosduma.models.Article;

public interface NewsInteractor {

    interface NewsCallback{

        void success(List<Article> articles);
        void failure(Exception e);

    }

    void loadArticles(int id, NewsCallback callback);
    void loadNews(int id, NewsCallback callback);

    boolean canShowInterstitialAd();
    void resetCounter();
    void incrementCounter();

}
