package ru.merkulyevsasha.gdnetwork.data

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import ru.merkulyevsasha.gdnetwork.models.AktResponse

interface AktsApi {

    @GET("/akts")
    fun getAkts(@Query("lastArticleReadDate") lastArticleReadDate: String): Single<List<AktResponse>>

    @PUT("/akts/{articleId}/like")
    fun likeAkt(@Path("articleId") articleId: Int): Single<AktResponse>

    @PUT("/akts/{articleId}/dislike")
    fun dislikeAkt(@Path("articleId") articleId: Int): Single<AktResponse>

    @GET("/akts/{articleId}")
    fun getAkt(@Path("articleId") articleId: Int): Single<AktResponse>

}
