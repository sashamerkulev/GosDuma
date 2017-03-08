package ru.merkulyevsasha.gosduma.presentation.news;

import com.google.firebase.crash.FirebaseCrash;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.merkulyevsasha.gosduma.data.http.RssParser;
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

    private Callback<ResponseBody> getSubscriber(final int id) {
        return new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        RssParser parser = new RssParser();
                        List<Article> result = parser.parseXml(response.body().string());

                        inter.saveToCache(id, result);

                        view.hideProgress();
                        view.showNews(result);
                    }
                    catch(Exception e){
                        view.hideProgress();
                        FirebaseCrash.report(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                FirebaseCrash.report(t);
                view.hideProgress();
            }
        };

    }

    public void refresh(int id){
        view.showProgress();
        inter.news(id).enqueue(getSubscriber(id));
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
