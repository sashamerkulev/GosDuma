package ru.merkulyevsasha.aktdetails

import io.reactivex.android.schedulers.AndroidSchedulers
import ru.merkulyevsasha.coreandroid.base.BasePresenterImpl
import ru.merkulyevsasha.gdcore.AktDistributor
import ru.merkulyevsasha.gdcore.domain.AktsInteractor
import ru.merkulyevsasha.gdcore.routers.GDMainActivityRouter
import ru.merkulyevsasha.gdcoreandroid.presentation.AktLikeClickHandler
import timber.log.Timber

class AktDetailsPresenterImpl(
    private val aktsInteractor: AktsInteractor,
    private val aktDistributor: AktDistributor,
    private val applicationRouter: GDMainActivityRouter
) : BasePresenterImpl<AktDetailsView>() {

    private val aktLikeClickHandler = AktLikeClickHandler(aktsInteractor,
        { addCommand { view?.updateItem(it) } },
        { view?.showError() })

    fun onFirstLoad(articleId: Int) {
        compositeDisposable.add(
            aktsInteractor.getAkt(articleId)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { addCommand { view?.showProgress() } }
                .doAfterTerminate { addCommand { view?.hideProgress() } }
                .subscribe(
                    { addCommand { view?.showItem(it) } },
                    {
                        Timber.e(it)
                        view?.showError()
                    }))
    }

    fun onAktLikeClicked(articleId: Int) {
        compositeDisposable.add(aktLikeClickHandler.onArticleLikeClicked(articleId))
    }

    fun onAktDislikeClicked(articleId: Int) {
        compositeDisposable.add(aktLikeClickHandler.onArticleDislikeClicked(articleId))
    }

    fun onAktCommentClicked(articleId: Int) {
        applicationRouter.showAktComments(articleId)
    }

    fun onAktShareClicked(articleId: Int) {
        compositeDisposable.add(
            aktsInteractor.getAkt(articleId)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { addCommand { view?.showProgress() } }
                .doAfterTerminate { addCommand { view?.hideProgress() } }
                .subscribe(
                    {
                        addCommand { aktDistributor.distribute(it) }
                    },
                    {
                        Timber.e(it)
                        view?.showError()
                    }))
    }
}