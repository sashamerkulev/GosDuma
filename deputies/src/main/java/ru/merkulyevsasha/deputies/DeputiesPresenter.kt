package ru.merkulyevsasha.deputies

import ru.merkulyevsasha.core.ResourceProvider
import ru.merkulyevsasha.coreandroid.base.BasePresenterImpl
import ru.merkulyevsasha.gdcore.domain.DeputiesInteractor

class DeputiesPresenter(
    private val deputiesInteractor: DeputiesInteractor,
    private val resourceProvider: ResourceProvider
) : BasePresenterImpl<DeputiesView>()