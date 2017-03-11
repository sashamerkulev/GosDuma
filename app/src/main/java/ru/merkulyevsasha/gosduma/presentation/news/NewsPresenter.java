package ru.merkulyevsasha.gosduma.presentation.news;

import com.google.firebase.crash.FirebaseCrash;

import java.util.List;

import ru.merkulyevsasha.gosduma.R;
import ru.merkulyevsasha.gosduma.domain.NewsInteractor;
import ru.merkulyevsasha.gosduma.models.Article;
import ru.merkulyevsasha.gosduma.presentation.MvpPresenter;
import ru.merkulyevsasha.gosduma.presentation.MvpView;


public class NewsPresenter implements MvpPresenter {

    private NewsView view;

    private NewsInteractor inter;

    public NewsPresenter(NewsInteractor inter){
        this.inter = inter;
    }

    @Override
    public void onStart(MvpView view) {
        this.view = (NewsView)view;
    }

    @Override
    public void onStop() {
        view = null;
    }

    public void refresh(int id){
        view.showProgress();
        inter.loadNews(id, new NewsInteractor.NewsCallback() {
            @Override
            public void success(List<Article> articles) {
                if (view == null)
                    return;

                view.hideProgress();
                view.showNews(articles);
            }

            @Override
            public void failure(Exception e) {
                FirebaseCrash.report(e);

                if (view == null)
                    return;

                view.hideProgress();
                view.showMessage(R.string.error_loading_news_message);
            }
        });
    }

    public void load(int id){
        List<Article> articles = inter.getArticles(id);
        if (articles.size() > 0){
            view.showNews(articles);
        } else {
            refresh(id);
        }

    }

}
