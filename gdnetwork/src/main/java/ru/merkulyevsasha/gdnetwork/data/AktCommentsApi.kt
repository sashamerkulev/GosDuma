package ru.merkulyevsasha.gdnetwork.data

import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import ru.merkulyevsasha.gdnetwork.models.AktCommentResponse

interface AktCommentsApi {

    @GET("/akts/{articleId}/comments")
    fun getAktComments(@Path("articleId") articleId: Int,
                           @Query("lastCommentsReadDate") lastCommentsReadDate: String?): Single<List<AktCommentResponse>>

    @DELETE("/akts/comments/{commentId}")
    fun deleteAktComment(@Path("commentId") commentId: Int): Completable

    @FormUrlEncoded
    @POST("/akts/{articleId}/comments")
    fun addAktComment(@Path("articleId") articleId: Int,
                          @Field("comments") comments: String): Single<AktCommentResponse>

    @PUT("/akts/comments/{commentId}/like")
    fun likeAktComment(@Path("commentId") commentId: Int): Single<AktCommentResponse>

    @PUT("/akts/comments/{commentId}/dislike")
    fun dislikeAktComment(@Path("commentId") commentId: Int): Single<AktCommentResponse>
}
