package ru.merkulyevsasha.gdnetrepository.network.base

import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.merkulyevsasha.core.preferences.KeyValueStorage
import ru.merkulyevsasha.netrepository.network.base.LoggingInterceptor
import ru.merkulyevsasha.netrepository.network.base.TokenInterceptor

open class CorBaseApiRepository(
    sharedPreferences: KeyValueStorage,
    baseUrl: String,
    debugMode: Boolean
) {
    internal val retrofit: Retrofit

    init {
        val builder = OkHttpClient.Builder()
        if (debugMode) {
//            val httpLoggingInterceptor = HttpLoggingInterceptor()
//            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC
//            builder.addInterceptor(httpLoggingInterceptor)
            builder.addNetworkInterceptor(StethoInterceptor())
        }
        builder.addNetworkInterceptor(LoggingInterceptor())
        builder.addInterceptor(TokenInterceptor(sharedPreferences))
        val client = builder.build()

//        val gson = GsonBuilder()
//            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
//            .create()

        retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
//            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }
}