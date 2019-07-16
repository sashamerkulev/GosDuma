package ru.merkulyevsasha.deputydetails

import ru.merkulyevsasha.core.ResourceProvider
import ru.merkulyevsasha.coreandroid.base.BasePresenterImpl
import ru.merkulyevsasha.gdcore.domain.DeputyDetailsInteractor

class DeputyDetailsPresenter(
    private val deputyDetailsInteractor: DeputyDetailsInteractor,
    private val resourceProvider: ResourceProvider
) : BasePresenterImpl<DeputyDetailsView>()
