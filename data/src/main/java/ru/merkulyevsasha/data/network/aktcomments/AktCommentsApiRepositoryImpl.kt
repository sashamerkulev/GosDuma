package ru.merkulyevsasha.data.network.aktcomments

import io.reactivex.Completable
import io.reactivex.Single
import ru.merkulyevsasha.core.preferences.KeyValueStorage
import ru.merkulyevsasha.data.network.base.BaseApiRepository
import ru.merkulyevsasha.data.network.mappers.AktCommentsMapper
import ru.merkulyevsasha.gdcore.models.AktComment
import ru.merkulyevsasha.gdcore.repositories.AktCommentsApiRepository
import ru.merkulyevsasha.gdnetwork.data.AktCommentsApi
import java.text.SimpleDateFormat
import java.util.*

class AktCommentsApiRepositoryImpl(
    sharedPreferences: KeyValueStorage,
    baseUrl: String,
    debugMode: Boolean
) : BaseApiRepository(sharedPreferences, baseUrl, debugMode), AktCommentsApiRepository {

    private val aktCommentMapper  by lazy { AktCommentsMapper("bearer " + sharedPreferences.getAccessToken(), baseUrl) }

    private val api: AktCommentsApi = retrofit.create(AktCommentsApi::class.java)

    private val format = "yyyy-MM-dd'T'HH:mm:ss"
    private val simpleDateFormat = SimpleDateFormat(format, Locale.getDefault())

    override fun getAktComments(articleId: Int, lastArticleCommentsReadDate: Date): Single<List<AktComment>> {
        return api.getAktComments(articleId, simpleDateFormat.format(lastArticleCommentsReadDate))
            .flattenAsFlowable { it }
            .map { aktCommentMapper.map(it) }
            .toList()
    }

    override fun addAktComment(articleId: Int, comment: String): Single<AktComment> {
        return api.addAktComment(articleId, comment)
            .map { aktCommentMapper.map(it) }
    }

    override fun likeAktComment(commentId: Int): Single<AktComment> {
        return api.likeAktComment(commentId)
            .map { aktCommentMapper.map(it) }
    }

    override fun dislikeAktComment(commentId: Int): Single<AktComment> {
        return api.dislikeAktComment(commentId)
            .map { aktCommentMapper.map(it) }
    }

    override fun deleteAktComment(commentId: Int): Completable {
        return api.deleteAktComment(commentId)
    }
}