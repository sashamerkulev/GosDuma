package ru.merkulyevsasha.akts

import io.reactivex.android.schedulers.AndroidSchedulers
import ru.merkulyevsasha.coreandroid.base.BasePresenterImpl
import ru.merkulyevsasha.gdcore.AktDistributor
import ru.merkulyevsasha.gdcore.domain.AktsInteractor
import ru.merkulyevsasha.gdcore.models.Akt
import ru.merkulyevsasha.gdcore.routers.GDMainActivityRouter
import ru.merkulyevsasha.gdcoreandroid.common.aktadapter.AktClickCallbackHandler
import ru.merkulyevsasha.gdcoreandroid.common.aktadapter.AktCommentCallbackClickHandler
import ru.merkulyevsasha.gdcoreandroid.common.aktadapter.AktLikeCallbackClickHandler
import ru.merkulyevsasha.gdcoreandroid.common.aktadapter.AktShareCallbackClickHandler
import ru.merkulyevsasha.gdcoreandroid.presentation.AktLikeClickHandler
import ru.merkulyevsasha.gdcoreandroid.presentation.SearchAktHandler
import timber.log.Timber

class AktsPresenterImpl(
    private val aktsInteractor: AktsInteractor,
    private val aktDistributor: AktDistributor,
    private val applicationRouter: GDMainActivityRouter
) : BasePresenterImpl<AktsView>(),
    AktClickCallbackHandler, AktLikeCallbackClickHandler, AktShareCallbackClickHandler, AktCommentCallbackClickHandler {

    private val aktLikeClickHandler = AktLikeClickHandler(aktsInteractor,
        { view?.updateItem(it) },
        { view?.showError() })

    private val searchAktHandler = SearchAktHandler(aktsInteractor, false,
        { view?.showProgress() },
        { view?.hideProgress() },
        { view?.showItems(it) },
        { view?.showError() })

    fun onFirstLoad() {
        compositeDisposable.add(
            aktsInteractor.getAkts()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { view?.showProgress() }
                .doAfterTerminate { view?.hideProgress() }
                .subscribe(
                    { view?.showItems(it) },
                    {
                        Timber.e(it)
                        view?.showError()
                    }))
    }

    fun onRefresh() {
        compositeDisposable.add(
            aktsInteractor.refreshAndGetAkts()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { view?.showProgress() }
                .doAfterTerminate { view?.hideProgress() }
                .subscribe(
                    { view?.updateItems(it) },
                    {
                        Timber.e(it)
                        view?.showError()
                    }))
    }

    fun onSearch(searchText: String?) {
        compositeDisposable.add(searchAktHandler.onSearchArticles(searchText))
    }

    override fun onAktCliked(item: Akt) {
        applicationRouter.showAktDetails(item.aktId)
    }

    override fun onAktLikeClicked(item: Akt) {
        compositeDisposable.add(aktLikeClickHandler.onArticleLikeClicked(item.aktId))
    }

    override fun onAktDislikeClicked(item: Akt) {
        compositeDisposable.add(aktLikeClickHandler.onArticleDislikeClicked(item.aktId))
    }

    override fun onAktCommentClicked(articleId: Int) {
        applicationRouter.showAktComments(articleId)
    }

    override fun onAktShareClicked(item: Akt) {
        aktDistributor.distribute(item)
    }

}