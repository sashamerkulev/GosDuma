package ru.merkulyevsasha.gdcoreandroid.presentation

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import ru.merkulyevsasha.gdcore.domain.AktsInteractor
import ru.merkulyevsasha.gdcore.models.Akt
import timber.log.Timber

class AktLikeClickHandler(
    private val articlesInteractor: AktsInteractor,
    private val succes: (Akt) -> Unit,
    private val failure: () -> Unit
) {
    fun onArticleLikeClicked(articleId: Int): Disposable {
        return articlesInteractor.likeAkt(articleId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { newItem -> succes(newItem) },
                {
                    Timber.e(it)
                    failure()
                })
    }

    fun onArticleDislikeClicked(articleId: Int): Disposable {
        return articlesInteractor.dislikeAkt(articleId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { newItem -> succes(newItem) },
                {
                    Timber.e(it)
                    failure()
                })
    }
}