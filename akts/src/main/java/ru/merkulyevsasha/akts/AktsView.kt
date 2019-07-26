package ru.merkulyevsasha.akts

import ru.merkulyevsasha.coreandroid.base.BaseView
import ru.merkulyevsasha.gdcore.models.Akt

interface AktsView : BaseView {
    fun showError()
    fun hideProgress()
    fun showProgress()
    fun showItems(items: List<Akt>)
    fun updateItems(items: List<Akt>)
    fun updateItem(item: Akt)
}
