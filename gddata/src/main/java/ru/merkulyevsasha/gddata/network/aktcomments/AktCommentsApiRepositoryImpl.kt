package ru.merkulyevsasha.gddata.network.aktcomments

import io.reactivex.Completable
import io.reactivex.Single
import ru.merkulyevsasha.core.preferences.KeyValueStorage
import ru.merkulyevsasha.data.network.base.BaseApiRepository
import ru.merkulyevsasha.gdcore.models.AktComment
import ru.merkulyevsasha.gdcore.repositories.AktCommentsApiRepository
import java.util.*

class AktCommentsApiRepositoryImpl(
    sharedPreferences: KeyValueStorage,
    baseUrl: String,
    debugMode: Boolean
) : BaseApiRepository(sharedPreferences, baseUrl, debugMode), AktCommentsApiRepository {
    override fun getAktComments(articleId: Int, lastArticleCommentsReadDate: Date): Single<List<AktComment>> {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun addAktComment(articleId: Int, comment: String): Single<AktComment> {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun likeAktComment(commentId: Int): Single<AktComment> {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun dislikeAktComment(commentId: Int): Single<AktComment> {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteAktComment(commentId: Int): Completable {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

}