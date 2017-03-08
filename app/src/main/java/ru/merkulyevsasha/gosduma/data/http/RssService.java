package ru.merkulyevsasha.gosduma.data.http;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;

public class RssService {

    private final RssInterface anInterface;

    private RssService(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.duma.gov.ru/")
                .build();

        anInterface = retrofit.create(RssInterface.class);
    }

    // https://habrahabr.ru/post/27108/
    private static volatile RssService mInstance;
    public static RssService getInstance() {
        if (mInstance == null) {
            synchronized (RssService.class) {
                if (mInstance == null) {
                    mInstance = new RssService();
                }
            }
        }
        return mInstance;
    }

    public Call<ResponseBody> gosduma() {
        return anInterface.gosduma();
    }

    public Call<ResponseBody> chairman() {
        return anInterface.chairman();
    }


}