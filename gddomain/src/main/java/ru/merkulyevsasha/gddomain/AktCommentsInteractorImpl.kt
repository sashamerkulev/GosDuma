package ru.merkulyevsasha.gddomain

import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import ru.merkulyevsasha.gdcore.database.GDDatabaseRepository
import ru.merkulyevsasha.gdcore.domain.AktCommentsInteractor
import ru.merkulyevsasha.gdcore.models.Akt
import ru.merkulyevsasha.gdcore.models.AktComment
import ru.merkulyevsasha.gdcore.preferences.SettingsSharedPreferences
import ru.merkulyevsasha.gdcore.repositories.AktCommentsApiRepository
import ru.merkulyevsasha.gdcore.repositories.AktsApiRepository
import ru.merkulyevsasha.gddomain.mappers.AktSourceNameMapper
import java.util.*

class AktCommentsInteractorImpl(
    private val aktsApiRepository: AktsApiRepository,
    private val aktCommentsApiRepository: AktCommentsApiRepository,
    private val keyValueStorage: SettingsSharedPreferences,
    private val databaseRepository: GDDatabaseRepository,
    private val sourceNameMapper: AktSourceNameMapper
) : AktCommentsInteractor {

    override fun getAktComments(articleId: Int): Single<Pair<Akt, List<AktComment>>> {
        return Single.zip(
            databaseRepository.getAkt(articleId),
            databaseRepository.getAktComments(articleId),
            BiFunction { t1: Akt, t2: List<AktComment> -> t1 to t2 }
        ).flatMap { pair ->
            if (pair.second.isEmpty()) refreshAndGetAktComments(articleId)
            else Single.just(pair)
        }
            .subscribeOn(Schedulers.io())
    }

    override fun refreshAndGetAktComments(articleId: Int): Single<Pair<Akt, List<AktComment>>> {
        return Single.fromCallable { keyValueStorage.getLastArticleCommentReadDate() ?: Date(0) }
            .flatMap {
                Single.zip(
                    aktsApiRepository.getAkt(articleId)
                        .doOnSuccess { item ->
                            databaseRepository.updateAkt(item)
                        }
                        .map { item ->
                            sourceNameMapper.map(item)
                        },
                    aktCommentsApiRepository.getAktComments(articleId, it)
                        .doOnSuccess { items ->
                            if (items.isNotEmpty()) {
                                databaseRepository.addOrUpdateAktComments(items)
                                keyValueStorage.setLastArticleCommentReadDate(Date())
                            }
                        }
                    ,
                    BiFunction { t1: Akt, t2: List<AktComment> -> t1 to t2 }
                )
            }
            .subscribeOn(Schedulers.io())
    }

    override fun addAktComment(articleId: Int, comment: String): Single<AktComment> {
        return aktCommentsApiRepository.addAktComment(articleId, comment)
            .doOnSuccess {
                databaseRepository.updateAktComment(it, 1)
            }
            .subscribeOn(Schedulers.io())
    }

    override fun likeAktComment(commentId: Int): Single<AktComment> {
        return aktCommentsApiRepository.likeAktComment(commentId)
            .subscribeOn(Schedulers.io())
    }

    override fun dislikeAktComment(commentId: Int): Single<AktComment> {
        return aktCommentsApiRepository.dislikeAktComment(commentId)
            .subscribeOn(Schedulers.io())
    }

    override fun deleteAktComment(commentId: Int): Completable {
        return aktCommentsApiRepository.deleteAktComment(commentId)
            .subscribeOn(Schedulers.io())
    }
}
