package ru.merkulyevsasha.akts

import io.reactivex.android.schedulers.AndroidSchedulers
import ru.merkulyevsasha.core.NewsDistributor
import ru.merkulyevsasha.core.routers.MainActivityRouter
import ru.merkulyevsasha.coreandroid.base.BasePresenterImpl
import ru.merkulyevsasha.gdcore.domain.AktsInteractor
import ru.merkulyevsasha.gdcore.models.Akt
import ru.merkulyevsasha.gdcoreandroid.common.aktadapter.AktClickCallbackHandler
import ru.merkulyevsasha.gdcoreandroid.common.aktadapter.AktCommentCallbackClickHandler
import ru.merkulyevsasha.gdcoreandroid.common.aktadapter.AktLikeCallbackClickHandler
import ru.merkulyevsasha.gdcoreandroid.common.aktadapter.AktShareCallbackClickHandler
import ru.merkulyevsasha.gdcoreandroid.presentation.AktLikeClickHandler
import ru.merkulyevsasha.gdcoreandroid.presentation.SearchAktHandler
import timber.log.Timber

class AktsPresenterImpl(
    private val articlesInteractor: AktsInteractor,
    private val newsDistributor: NewsDistributor,
    private val applicationRouter: MainActivityRouter
) : BasePresenterImpl<AktsView>(),
    AktClickCallbackHandler, AktLikeCallbackClickHandler, AktShareCallbackClickHandler, AktCommentCallbackClickHandler {

    private val articleLikeClickHandler = AktLikeClickHandler(articlesInteractor,
        { view?.updateItem(it) },
        { view?.showError() })

    private val searchArticleHandler = SearchAktHandler(articlesInteractor, false,
        { view?.showProgress() },
        { view?.hideProgress() },
        { view?.showItems(it) },
        { view?.showError() })

    fun onFirstLoad() {
        compositeDisposable.add(
            articlesInteractor.getAkts()
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
            articlesInteractor.refreshAndGetAkts()
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
        compositeDisposable.add(searchArticleHandler.onSearchArticles(searchText))
    }

    override fun onArticleCliked(item: Akt) {
        applicationRouter.showArticleDetails(item.articleId)
    }

    override fun onArticleLikeClicked(item: Akt) {
        compositeDisposable.add(articleLikeClickHandler.onArticleLikeClicked(item.articleId))
    }

    override fun onArticleDislikeClicked(item: Akt) {
        compositeDisposable.add(articleLikeClickHandler.onArticleDislikeClicked(item.articleId))
    }

    override fun onCommentArticleClicked(articleId: Int) {
        applicationRouter.showArticleComments(articleId)
    }

    override fun onArticleShareClicked(item: Akt) {
        //newsDistributor.distribute(item)
    }

}