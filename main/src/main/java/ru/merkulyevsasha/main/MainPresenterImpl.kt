package ru.merkulyevsasha.main

import io.reactivex.android.schedulers.AndroidSchedulers
import ru.merkulyevsasha.core.domain.SetupInteractor
import ru.merkulyevsasha.coreandroid.base.BasePresenterImpl
import timber.log.Timber

class MainPresenterImpl(private val setupInteractor: SetupInteractor) : BasePresenterImpl<ru.merkulyevsasha.main.MainView>() {
    fun onSetup() {
        compositeDisposable.add(
            setupInteractor.registerSetup()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { addCommand {  view?.showMainScreen() }},
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
