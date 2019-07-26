package ru.merkulyevsasha.aktdetails

import io.reactivex.android.schedulers.AndroidSchedulers
import ru.merkulyevsasha.core.NewsDistributor
import ru.merkulyevsasha.core.routers.MainActivityRouter
import ru.merkulyevsasha.coreandroid.base.BasePresenterImpl
import ru.merkulyevsasha.gdcore.domain.AktsInteractor
import ru.merkulyevsasha.gdcoreandroid.presentation.AktLikeClickHandler
import timber.log.Timber

class AktDetailsPresenterImpl(
    private val articlesInteractor: AktsInteractor,
    private val newsDistributor: NewsDistributor,
    private val applicationRouter: MainActivityRouter
) : BasePresenterImpl<AktDetailsView>() {

    private val articleLikeClickHandler = AktLikeClickHandler(articlesInteractor,
        { view?.updateItem(it) },
        { view?.showError() })

    fun onFirstLoad(articleId: Int) {
        compositeDisposable.add(
            articlesInteractor.getAkt(articleId)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { view?.showProgress() }
                .doAfterTerminate { view?.hideProgress() }
                .subscribe(
                    { view?.showItem(it) },
                    {
                        Timber.e(it)
                        view?.showError()
                    }))
    }

    fun onArticleLikeClicked(articleId: Int) {
        compositeDisposable.add(articleLikeClickHandler.onArticleLikeClicked(articleId))
    }

    fun onArticleDislikeClicked(articleId: Int) {
        compositeDisposable.add(articleLikeClickHandler.onArticleDislikeClicked(articleId))
    }

    fun onCommentClicked(articleId: Int) {
        applicationRouter.showArticleComments(articleId)
    }

    fun onShareClicked(articleId: Int) {
        compositeDisposable.add(
            articlesInteractor.getAkt(articleId)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { view?.showProgress() }
                .doAfterTerminate { view?.hideProgress() }
                .subscribe(
                    {
                        //newsDistributor.distribute(it)
                    },
                    {
                        Timber.e(it)
                        view?.showError()
                    }))
    }
}