package ru.merkulyevsasha.gosduma.presentation.news;

import com.google.android.gms.ads.InterstitialAd;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import ru.merkulyevsasha.gosduma.R;
import ru.merkulyevsasha.gosduma.domain.NewsInteractor;
import ru.merkulyevsasha.gosduma.models.Article;
import ru.merkulyevsasha.gosduma.presentation.MvpPresenter;


public class NewsPresenter extends MvpPresenter<NewsView> {

    private final NewsInteractor inter;

    @Inject
    public NewsPresenter(NewsInteractor inter){
        this.inter = inter;
    }

    void refresh(int id){
        if (view == null) return;
        view.showProgress();
        compositeDisposable.add(inter.getNews(id).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Article>>() {
                    @Override
                    public void accept(@NonNull List<Article> articles) {
                        if (view == null) return;
                        view.hideProgress();
                        view.showNews(articles);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) {
                        if (view == null) return;
                        view.hideProgress();
                        view.showMessage(R.string.error_loading_news_message);
                    }
                }));
    }

    public void load(final int id){
        if (view == null) return;
        view.showProgress();
        compositeDisposable.add(inter.getArticles(id).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Article>>() {
                    @Override
                    public void accept(@NonNull List<Article> articles) {
                        if (view == null) return;
                        view.hideProgress();
                        if (articles.size() > 0){
                            view.showNews(articles);
                        } else {
                            refresh(id);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) {
                        if (view == null) return;
                        view.hideProgress();
                        view.showMessage(R.string.error_loading_news_message);
                    }
                }));
    }

    void onItemClicked(Article item, InterstitialAd interstitialAd) {
        if (view == null) return;
        view.showDetailsItem(item);
        if (inter.canShowInterstitialAd()) {
            if (interstitialAd != null && interstitialAd.isLoaded()) {
                inter.resetCounter();
                interstitialAd.show();
            }
        } else {
            inter.incrementCounter();
        }

    }
}
