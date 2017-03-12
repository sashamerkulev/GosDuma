package ru.merkulyevsasha.gosduma.data.http;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;

public class NewsRssService {

    interface NewsRssInterface {

        @GET("news/273/rss/")
        Call<ResponseBody> gosduma();

        @GET("news/274/rss/")
        Call<ResponseBody> chairman();

    }

    private final NewsRssInterface anInterface;

    public NewsRssService(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.duma.gov.ru/")
                .build();

        anInterface = retrofit.create(NewsRssInterface.class);
    }

    public Call<ResponseBody> gosduma() {
        return anInterface.gosduma();
    }

    public Call<ResponseBody> chairman() {
        return anInterface.chairman();
    }


}
