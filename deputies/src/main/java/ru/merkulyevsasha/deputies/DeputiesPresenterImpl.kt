package ru.merkulyevsasha.deputies

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.merkulyevsasha.core.ResourceProvider
import ru.merkulyevsasha.coreandroid.base.BasePresenterImpl
import ru.merkulyevsasha.gdcore.domain.DeputiesInteractor
import ru.merkulyevsasha.gdcore.routers.GDMainActivityRouter

class DeputiesPresenterImpl(
    private val deputiesInteractor: DeputiesInteractor,
    private val router: GDMainActivityRouter,
    private val resourceProvider: ResourceProvider
) : BasePresenterImpl<DeputiesView>(), OnDeputyClickListener {

    var job: Job? = null

    override fun onDestroy() {
        job?.cancel()
        job = null
        super.onDestroy()
    }

    fun onFirstLoad() {
        job = GlobalScope.launch(Dispatchers.Main) {
            try {
                addCommand { view?.showProgress() }
                val result = deputiesInteractor.getDeputies("", "")
                addCommand { view?.showItems(result.deputies) }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                addCommand { view?.hideProgress() }
            }
        }
    }

    fun onRefresh() {
        addCommand { view?.hideProgress() }
    }

    override fun onDeputyClicked(deputyId: Int) {
        router.showDeputyDetails(deputyId)
    }

}