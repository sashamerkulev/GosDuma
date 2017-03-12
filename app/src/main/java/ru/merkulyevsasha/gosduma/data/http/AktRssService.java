package ru.merkulyevsasha.gosduma.data.http;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public class AktRssService {

    interface AktRssInterface {

        @GET("Rss/{block}")
        Call<ResponseBody> aktPresident(@Query("block") String block);

        @GET("Rss/block={block}/org={org}")
        Call<ResponseBody> aktGoverment(@Path("block") String block, @Path("org") String org);

        @GET("Rss/org={org}/type={type}")
        Call<ResponseBody> aktSovetfed(@Path("org") String org, @Path("type") String type);

        @GET("Rss/org={org}")
        Call<ResponseBody> aktGosduma(@Query("org") String org);


    }

    private final AktRssInterface anInterface;

    public AktRssService(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://publication.pravo.gov.ru/")
                .build();

        anInterface = retrofit.create(AktRssInterface.class);
    }

    public Call<ResponseBody> aktPresident() {
        return anInterface.aktPresident("E94B6872-DCAC-414F-B2F1-A538D13A12A0");
    }

    public Call<ResponseBody> aktGoverment() {
        return anInterface.aktGoverment("1E57A3E5-9122-41E5-B3CF-0AB68ED3601A", "8005D8C9-4B6D-48D3-861A-2A37E69FCCB3");
    }

    public Call<ResponseBody> aktSovetfed() {
        return anInterface.aktSovetfed("730E580C-C6AD-4ACA-BE1A-B49F7E4694FE", "FD5A8766-F6FD-4AC2-8FD9-66F414D314AC");
    }

    public Call<ResponseBody> aktGosduma() {
        return anInterface.aktGosduma("1E57A3E5-9122-41E5-B3CF-0AB68ED3601A");
    }


}
