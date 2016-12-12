package ru.merkulyevsasha.gosduma.http;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;


interface RssInterface {


    @GET("news/273/rss/")
    Call<ResponseBody> gosduma();

    @GET("news/274/rss/")
    Call<ResponseBody> chairman();

}
