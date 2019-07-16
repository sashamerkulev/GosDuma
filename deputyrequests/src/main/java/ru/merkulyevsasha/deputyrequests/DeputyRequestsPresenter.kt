package ru.merkulyevsasha.deputyrequests

import ru.merkulyevsasha.core.ResourceProvider
import ru.merkulyevsasha.coreandroid.base.BasePresenterImpl
import ru.merkulyevsasha.gdcore.domain.DeputyRequestsInteractor

class DeputyRequestsPresenter(
    private val deputyRequestsInteractor: DeputyRequestsInteractor,
    private val resourceProvider: ResourceProvider
) : BasePresenterImpl<DeputyRequestsView>()