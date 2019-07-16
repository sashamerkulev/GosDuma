package ru.merkulyevsasha.gosduma.presentation.main

import ru.merkulyevsasha.coreandroid.base.BaseView

interface MainView : BaseView {
    fun showMainScreen()
    fun showFatalError()
}