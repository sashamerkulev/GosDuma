package ru.merkulyevsasha.gosduma.presentation.main

import io.reactivex.android.schedulers.AndroidSchedulers
import ru.merkulyevsasha.core.domain.SetupInteractor
import ru.merkulyevsasha.coreandroid.base.BasePresenterImpl
import timber.log.Timber

class MainPresenter(private val setupInteractor: SetupInteractor) : BasePresenterImpl<MainView>() {
    fun onSetup() {
        compositeDisposable.add(
            setupInteractor.registerSetup()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { view?.showMainScreen() },
                    {
                        Timber.e(it)
                        view?.showFatalError()
                    }))
    }

    fun onUpdateFirebaseId(firebaseId: String) {
        compositeDisposable.add(
            setupInteractor.updateFirebaseToken(firebaseId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { },
                    {
                        Timber.e(it)
                    }))
    }
}
