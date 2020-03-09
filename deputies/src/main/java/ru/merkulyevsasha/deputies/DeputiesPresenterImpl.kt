package ru.merkulyevsasha.deputies

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.merkulyevsasha.core.ResourceProvider
import ru.merkulyevsasha.coreandroid.base.BasePresenterImpl
import ru.merkulyevsasha.gdcore.domain.DeputiesInteractor

class DeputiesPresenterImpl(
    private val deputiesInteractor: DeputiesInteractor,
    private val resourceProvider: ResourceProvider
) : BasePresenterImpl<DeputiesView>() {

    var job: Job? = null

    fun onFirstLoad() {
        job = GlobalScope.launch(Dispatchers.Main) {
            try {
                val result = deputiesInteractor.getDeputies("", "")
                addCommand { view?.showItems(result.deputies) }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onDestroy() {
        job?.cancel()
        job = null
        super.onDestroy()
    }
}