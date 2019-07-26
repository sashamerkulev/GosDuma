package ru.merkulyevsasha.gdcoreandroid.presentation

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import ru.merkulyevsasha.gdcore.domain.AktInteractor
import ru.merkulyevsasha.gdcore.models.Akt
import timber.log.Timber

class SearchAktHandler(
    private val articlesInteractor: AktInteractor,
    private val byUserActivities: Boolean,
    private val showProgress: () -> Unit,
    private val hideProgress: () -> Unit,
    private val succes: (List<Akt>) -> Unit,
    private val failure: () -> Unit
) {
    fun onSearchArticles(searchText: String?): Disposable {
        return articlesInteractor.searchArticles(searchText, byUserActivities)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showProgress() }
            .doAfterTerminate { hideProgress() }
            .subscribe(
                { succes(it) },
                {
                    Timber.e(it)
                    failure()
                })
    }
}