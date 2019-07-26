package ru.merkulyevsasha.aktcomments

import io.reactivex.android.schedulers.AndroidSchedulers
import ru.merkulyevsasha.core.NewsDistributor
import ru.merkulyevsasha.core.models.ArticleOrComment
import ru.merkulyevsasha.coreandroid.base.BasePresenterImpl
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
    private val articleCommentsInteractor: AktCommentsInteractor,
    articlesInteractor: AktsInteractor,
    private val newsDistributor: NewsDistributor
) : BasePresenterImpl<AktCommentsView>(),
    AktLikeCallbackClickHandler, AktShareCallbackClickHandler, AktCommentLikeCallbackClickHandler, AktCommentShareCallbackClickHandler {

    private val articleLikeClickHandler = AktLikeClickHandler(articlesInteractor,
        { view?.updateItem(it) },
        { view?.showError() })

    fun onFirstLoad(articleId: Int) {
        compositeDisposable.add(
            articleCommentsInteractor.getAktComments(articleId)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { view?.showProgress() }
                .doAfterTerminate { view?.hideProgress() }
                .subscribe({
                    val result = listOf<ArticleOrComment>(it.first) + it.second
                    view?.showComments(result)
                }, {
                    Timber.e(it)
                    view?.showError()
                }))
    }

    fun onRefresh(articleId: Int) {
        compositeDisposable.add(
            articleCommentsInteractor.refreshAndGetAktComments(articleId)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { view?.showProgress() }
                .doAfterTerminate { view?.hideProgress() }
                .subscribe({
                    val aaa = listOf<ArticleOrComment>(it.first) + it.second
                    view?.showComments(aaa)
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
            articleCommentsInteractor.addAktComment(articleId, comment)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { view?.showProgress() }
                .doAfterTerminate { view?.hideProgress() }
                .subscribe({
                    view?.updateCommentItem(it)
                }, {
                    Timber.e(it)
                    view?.showError()
                }))
    }

    override fun onArticleLikeClicked(item: Akt) {
        compositeDisposable.add(articleLikeClickHandler.onArticleLikeClicked(item.articleId))
    }

    override fun onArticleDislikeClicked(item: Akt) {
        compositeDisposable.add(articleLikeClickHandler.onArticleDislikeClicked(item.articleId))
    }

    override fun onArticleShareClicked(item: Akt) {
    }

    override fun onCommentLikeClicked(item: AktComment) {
        compositeDisposable.add(
            articleCommentsInteractor.likeAktComment(item.commentId)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { view?.showProgress() }
                .doAfterTerminate { view?.hideProgress() }
                .subscribe({
                    view?.updateCommentItem(it)
                }, {
                    Timber.e(it)
                    view?.showError()
                }))
    }

    override fun onCommentDislikeClicked(item: AktComment) {
        compositeDisposable.add(
            articleCommentsInteractor.dislikeAktComment(item.commentId)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { view?.showProgress() }
                .doAfterTerminate { view?.hideProgress() }
                .subscribe({
                    view?.updateCommentItem(it)
                }, {
                    Timber.e(it)
                    view?.showError()
                }))
    }

    override fun onCommentShareClicked(item: AktComment) {
        //newsDistributor.distribute(item)
    }

}