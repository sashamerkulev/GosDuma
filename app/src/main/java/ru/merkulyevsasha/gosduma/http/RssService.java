package ru.merkulyevsasha.gosduma.http;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.schedulers.Schedulers;

public class RssService {

    private final RssInterface anInterface;

    private RssService(){
//        Gson gson = new GsonBuilder()
//                .setDateFormat("yyyy-MM-dd")
//                .create();

        RxJavaCallAdapterFactory adapter = RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.duma.gov.ru/")
                //.addConverterFactory(GsonConverterFactory.create(gson))
                //.addCallAdapterFactory(adapter)
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
