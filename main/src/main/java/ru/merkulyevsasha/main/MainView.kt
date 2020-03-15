package ru.merkulyevsasha.main

import ru.merkulyevsasha.coreandroid.base.BaseView

interface MainView : BaseView {
    fun showMainScreen()
    fun showFatalError()
}