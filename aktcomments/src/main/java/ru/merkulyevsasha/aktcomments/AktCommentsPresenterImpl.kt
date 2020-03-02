package ru.merkulyevsasha.aktcomments

import io.reactivex.android.schedulers.AndroidSchedulers
import ru.merkulyevsasha.core.models.ArticleOrComment
import ru.merkulyevsasha.coreandroid.base.BasePresenterImpl
import ru.merkulyevsasha.gdcore.AktDistributor
import ru.merkulyevsasha.gdcore.domain.AktCommentsInteractor
import ru.merkulyevsasha.gdcore.domain.AktsInteractor
import ru.merkulyevsasha.gdcore.models.Akt
import ru.merkulyevsasha.gdcore.models.AktComment
import ru.merkulyevsasha.gdcoreandroid.common.aktadapter.AktCommentLikeCallbackClickHandler
import ru.merkulyevsasha.gdcoreandroid.common.aktadapter.AktCommentShareCallbackClickHandler
import ru.merkulyevsasha.gdcoreandroid.common.aktadapter.AktLikeCallbackClickHandler
import ru.merkulyevsasha.gdcoreandroid.common.aktadapter.AktShareCallbackClickHandler
import ru.merkulyevsasha.gdcoreandroid.presentation.AktLikeClickHandler
import timber.log.Timber

class AktCommentsPresenterImpl(
    private val aktCommentsInteractor: AktCommentsInteractor,
    aktsInteractor: AktsInteractor,
    private val aktDistributor: AktDistributor
) : BasePresenterImpl<AktCommentsView>(),
    AktLikeCallbackClickHandler, AktShareCallbackClickHandler, AktCommentLikeCallbackClickHandler, AktCommentShareCallbackClickHandler {

    private val aktLikeClickHandler = AktLikeClickHandler(aktsInteractor,
        { addCommand { view?.updateItem(it) } },
        { view?.showError() })

    fun onFirstLoad(articleId: Int) {
        compositeDisposable.add(
            aktCommentsInteractor.getAktComments(articleId)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { addCommand { view?.showProgress() } }
                .doAfterTerminate { addCommand { view?.hideProgress() } }
                .subscribe({
                    val result = listOf<ArticleOrComment>(it.first) + it.second
                    addCommand { view?.showComments(result) }
                }, {
                    Timber.e(it)
                    view?.showError()
                }))
    }

    fun onRefresh(articleId: Int) {
        compositeDisposable.add(
            aktCommentsInteractor.refreshAndGetAktComments(articleId)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { addCommand { view?.showProgress() } }
                .doAfterTerminate { addCommand { view?.hideProgress() } }
                .subscribe({
                    val aaa = listOf<ArticleOrComment>(it.first) + it.second
                    addCommand { view?.showComments(aaa) }
                }, {
                    Timber.e(it)
                    view?.showError()
                }))
    }

    fun onAddCommentClicked(articleId: Int, comment: String) {
        if (comment.isEmpty()) {
            view?.showError()
            return
        }
        compositeDisposable.add(
            aktCommentsInteractor.addAktComment(articleId, comment)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { addCommand { view?.showProgress() } }
                .doAfterTerminate { addCommand { view?.hideProgress() } }
                .subscribe({
                    addCommand { view?.updateCommentItem(it) }
                }, {
                    Timber.e(it)
                    view?.showError()
                }))
    }

    override fun onAktLikeClicked(item: Akt) {
        compositeDisposable.add(aktLikeClickHandler.onArticleLikeClicked(item.aktId))
    }

    override fun onAktDislikeClicked(item: Akt) {
        compositeDisposable.add(aktLikeClickHandler.onArticleDislikeClicked(item.aktId))
    }

    override fun onAktShareClicked(item: Akt) {
        aktDistributor.distribute(item)
    }

    override fun onAktCommentLikeClicked(item: AktComment) {
        compositeDisposable.add(
            aktCommentsInteractor.likeAktComment(item.commentId)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { addCommand { view?.showProgress() } }
                .doAfterTerminate { addCommand { view?.hideProgress() } }
                .subscribe({
                    addCommand { view?.updateCommentItem(it) }
                }, {
                    Timber.e(it)
                    view?.showError()
                }))
    }

    override fun onAktCommentDislikeClicked(item: AktComment) {
        compositeDisposable.add(
            aktCommentsInteractor.dislikeAktComment(item.commentId)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { addCommand { view?.showProgress() } }
                .doAfterTerminate { addCommand { view?.hideProgress() } }
                .subscribe({
                    addCommand { view?.updateCommentItem(it) }
                }, {
                    Timber.e(it)
                    view?.showError()
                }))
    }

    override fun onAktCommentShareClicked(item: AktComment) {
        aktDistributor.distribute(item)
    }

}