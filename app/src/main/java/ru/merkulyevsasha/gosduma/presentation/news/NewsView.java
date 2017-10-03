package ru.merkulyevsasha.gosduma.presentation.news;

import java.util.List;

import ru.merkulyevsasha.gosduma.models.Article;
import ru.merkulyevsasha.gosduma.presentation.MvpView;


interface NewsView extends MvpView{
    void showNews(List<Article> articles);
    void showDetailsItem(Article item);
}
