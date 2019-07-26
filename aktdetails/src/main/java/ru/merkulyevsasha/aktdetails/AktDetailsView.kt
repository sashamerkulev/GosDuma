package ru.merkulyevsasha.aktdetails

import ru.merkulyevsasha.coreandroid.base.BaseView
import ru.merkulyevsasha.gdcore.models.Akt

interface AktDetailsView : BaseView {
    fun showProgress()
    fun hideProgress()
    fun showError()
    fun showItem(item: Akt)
    fun updateItem(item: Akt)
}
