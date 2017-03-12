package ru.merkulyevsasha.gosduma.data;


import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import ru.merkulyevsasha.gosduma.models.Article;

public interface NewsRepository {

    List<Article> getArticles(int id);
    void saveToCache(int id, List<Article> result);

    Call<ResponseBody> gosduma();
    Call<ResponseBody> chairman();

    Call<ResponseBody> aktPresident();
    Call<ResponseBody> aktGoverment();
    Call<ResponseBody> aktSovetfed();
    Call<ResponseBody> aktGosduma();


}
