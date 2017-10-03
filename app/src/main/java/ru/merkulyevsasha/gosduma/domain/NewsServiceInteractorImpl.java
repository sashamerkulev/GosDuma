package ru.merkulyevsasha.gosduma.domain;


import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.Call;
import ru.merkulyevsasha.gosduma.R;
import ru.merkulyevsasha.gosduma.data.NewsRepository;
import ru.merkulyevsasha.gosduma.data.http.RssParser;
import ru.merkulyevsasha.gosduma.models.Article;
import ru.merkulyevsasha.gosduma.models.News;

public class NewsServiceInteractorImpl implements NewsServiceInteractor {

    private final HashMap<Integer, Integer> newsIds;
    private final NewsRepository repo;
    private final NewsInteractor news;
    private final Scheduler scheduler;

    public NewsServiceInteractorImpl(NewsRepository repo, NewsInteractor news, Scheduler scheduler) {

        this.repo = repo;
        this.news = news;
        this.scheduler = scheduler;

        newsIds = new HashMap<>();
        newsIds.put(R.id.nav_news_gd, R.string.menu_news_gd);
        newsIds.put(R.id.nav_news_preds, R.string.menu_news_preds);
        newsIds.put(R.id.nav_akt_pres, R.string.menu_akt_pres);
        newsIds.put(R.id.nav_akt_gover, R.string.menu_akt_gover);
        newsIds.put(R.id.nav_akt_sf, R.string.menu_akt_sf);
        newsIds.put(R.id.nav_akt_gd, R.string.menu_akt_gd);

    }

    @Override
    public Single<News> getNotificationNews2() {
        return Single.fromPublisher(new Publisher<News>() {
            @Override
            public void subscribe(Subscriber<? super News> subscriber) {

                RssParser parser = new RssParser();
                int id = 1;
                for (Map.Entry<Integer, Integer> entry : newsIds.entrySet()) {

                    try {
                        Call<ResponseBody> resp = news.getCallResponseBody(entry.getKey());
                        if (resp == null)
                            continue;

                        ResponseBody body = resp.execute().body();
                        List<Article> netNews = parser.parseXml(body.string());
                        if (netNews.size() == 0) continue;

                        List<Article> dbNews = repo.getArticles(entry.getKey());
                        repo.saveToCache(entry.getKey(), netNews);

                        if (dbNews.size() == 0) {
                            News news = new News();
                            news.setNavId(id++);
                            news.setTitleId(newsIds.get(entry.getKey()));
                            news.setName(netNews.get(0).Title);
                            subscriber.onNext(news);
                            continue;
                        }

                        Collections.sort(dbNews, new Comparator<Article>() {
                            @Override
                            public int compare(Article o1, Article o2) {
                                return o1.PubDate.compareTo(o2.PubDate);
                            }
                        });

                        Collections.sort(netNews, new Comparator<Article>() {
                            @Override
                            public int compare(Article o1, Article o2) {
                                return o1.PubDate.compareTo(o2.PubDate);
                            }
                        });

                        Article lastNetItem = netNews.get(netNews.size() - 1);
                        Article lastDbItem = dbNews.get(dbNews.size() - 1);
                        if (lastDbItem.PubDate.before(lastNetItem.PubDate)) {
                            News news = new News();
                            news.setNavId(id++);
                            news.setTitleId(newsIds.get(entry.getKey()));
                            news.setName(lastNetItem.Title);
                            subscriber.onNext(news);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                subscriber.onComplete();
            }
        }).observeOn(scheduler);
    }

}
