package ru.merkulyevsasha.gosduma.domain;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import ru.merkulyevsasha.gosduma.R;
import ru.merkulyevsasha.gosduma.data.NewsRepository;
import ru.merkulyevsasha.gosduma.data.http.RssParser;
import ru.merkulyevsasha.gosduma.models.Article;
import ru.merkulyevsasha.gosduma.models.News;

public class NewsServiceInteractorImpl implements NewsServiceInteractor {

    private HashMap<Integer, Integer> newsIds;
    private NewsRepository repo;

    public NewsServiceInteractorImpl(NewsRepository repo){

        this.repo = repo;

        newsIds = new HashMap<>();
        newsIds.put(R.id.nav_news_gd, R.string.menu_news_gd);
        newsIds.put(R.id.nav_news_preds, R.string.menu_news_preds);
        newsIds.put(R.id.nav_akt_pres, R.string.menu_akt_pres);
        newsIds.put(R.id.nav_akt_gover, R.string.menu_akt_gover);
        newsIds.put(R.id.nav_akt_sf, R.string.menu_akt_sf);
        newsIds.put(R.id.nav_akt_gd, R.string.menu_akt_gd);

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
    public List<News> getNotificationNews(){
        List<News> result = new ArrayList<>();
        RssParser parser = new RssParser();

        for (Map.Entry<Integer, Integer> entry : newsIds.entrySet()) {

            try {
                Call<ResponseBody> resp = getCallResponseBody(entry.getKey());
                if (resp == null)
                    continue;

                List<Article> dbNews = repo.getArticles(entry.getKey());

                ResponseBody body = resp.execute().body();

                List<Article> netNews = parser.parseXml(body.string());
                repo.saveToCache(entry.getKey(), netNews);

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
                if (dbNews.size() == 0){
                    News news = new News();
                    news.setNavId(entry.getKey());
                    news.setTitleId(newsIds.get(entry.getKey()));
                    news.setName(lastNetItem.Title);
                    result.add(news);
                } else {

                    Article lastDbItem = dbNews.get(dbNews.size() - 1);

                    if (lastDbItem.PubDate.before(lastNetItem.PubDate)) {
                        News news = new News();
                        news.setNavId(entry.getKey());
                        news.setTitleId(newsIds.get(entry.getKey()));
                        news.setName(lastNetItem.Title);
                        result.add(news);
                    }
                }
            } catch(Exception e){
                System.out.println("getNotificationNews: exception:"+e.getMessage());
                e.printStackTrace();
            }

        }


        return result;
    }

}
