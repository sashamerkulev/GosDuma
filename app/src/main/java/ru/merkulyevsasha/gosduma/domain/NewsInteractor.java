package ru.merkulyevsasha.gosduma.domain;


import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import ru.merkulyevsasha.gosduma.models.Article;

public interface NewsInteractor {

    List<Article> getArticles(int id);
    void saveToCache(int id, List<Article> result);

    Call<ResponseBody> news(int id);


}
