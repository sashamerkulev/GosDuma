package ru.merkulyevsasha.gosduma.domain;


import java.util.ArrayList;
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

public class NewsServiceInteractorImpl extends NewsInteractorImpl implements NewsServiceInteractor {

    private HashMap<Integer, Integer> newsIds;


    public NewsServiceInteractorImpl(NewsRepository repo){
        super(repo);

        newsIds = new HashMap<>();

        newsIds.put(R.id.nav_news_gd, R.string.menu_news_gd);
        newsIds.put(R.id.nav_news_preds, R.string.menu_news_preds);

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


            } catch(Exception e){
                e.printStackTrace();
            }

        }


        return result;
    }

}
