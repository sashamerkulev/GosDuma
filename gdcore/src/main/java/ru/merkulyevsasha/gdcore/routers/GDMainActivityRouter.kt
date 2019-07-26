package ru.merkulyevsasha.gdcore.routers

import ru.merkulyevsasha.core.routers.MainActivityRouter

interface GDMainActivityRouter : MainActivityRouter{
    fun showDeputyDetails()
    fun showLawDetails()
    fun showAktDetails(articleId: Int)
    fun showAktComments(articleId: Int)
}