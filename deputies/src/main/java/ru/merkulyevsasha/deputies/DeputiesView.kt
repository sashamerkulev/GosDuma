package ru.merkulyevsasha.deputies

import ru.merkulyevsasha.coreandroid.base.BaseView
import ru.merkulyevsasha.gdcore.models.Deputy

interface DeputiesView : BaseView {
    fun showItems(items: List<Deputy>)
    fun showProgress()
    fun hideProgress()
}

